import * as cdk from '@aws-cdk/core';
import * as ec2 from '@aws-cdk/aws-ec2';
import * as eks from '@aws-cdk/aws-eks';
import * as iam from '@aws-cdk/aws-iam';
import * as dynamodb from '@aws-cdk/aws-dynamodb';

import { AwsLoadBalancerController } from './aws-loadbalancer-controller';

export interface EksStackProps extends cdk.StackProps {
  vpcId?: string;
  clusterName?: string;
}

export class EksStack extends cdk.Stack {

  constructor(scope: cdk.App, id: string, props: EksStackProps) {
    super(scope, id, props);

    const masterRole = new iam.Role(this, 'ClusterMasterRole', {
      assumedBy: new iam.AccountRootPrincipal(),
    });

    // Create a EKS cluster with Fargate profile.
    const cluster = new eks.FargateCluster(this, 'EksCluster', {
      version: eks.KubernetesVersion.V1_20,
      mastersRole: masterRole,
      clusterName: props.clusterName,
      outputClusterName: true,

      endpointAccess: eks.EndpointAccess.PUBLIC,
      vpc: props.vpcId == undefined ? undefined : ec2.Vpc.fromLookup(this, 'vpc', { vpcId: props?.vpcId }),
      vpcSubnets: [{ subnetType: ec2.SubnetType.PRIVATE }],
    });

    new AwsLoadBalancerController(this, 'AwsLoadbalancerController', {
      eksCluster: cluster,
    });

    // Now we add the cdk8s chart for the actual application workload, here we take the nginx deployment & service as example.
    //
    // First we create an IAM role, which will be associated with the K8S service account for the actual k8s app. Then we
    // can grant permission to that IAM role so that the actual K8S app can access AWS resources as required.
    //
    // Please note the nginx app itself does not really need any access to AWS resources, however we still include the codes of
    // setting up IAM role and K8S service account so you can reuse them in your own use case where the K8S app does need to access
    // AWS resources, such as s3 buckets.
    //
    const appNamespace = 'default';
    const appServiceAccount = 'sa-app';
    const conditions = new cdk.CfnJson(this, 'ConditionJson', {
      value: {
        [`${cluster.clusterOpenIdConnectIssuer}:aud`]: 'sts.amazonaws.com',
        [`${cluster.clusterOpenIdConnectIssuer}:sub`]: `system:serviceaccount:${appNamespace}:${appServiceAccount}`,
      },
    });

    const iamPrinciple = new iam.FederatedPrincipal(
      cluster.openIdConnectProvider.openIdConnectProviderArn,
      {},
      'sts:AssumeRoleWithWebIdentity'
    ).withConditions({
      StringEquals: conditions,
    });

    const iamRoleForSa = new iam.Role(this, 'AppSaRole', {
      assumedBy: iamPrinciple,
    });

    const table = dynamodb.Table.fromTableName(this, 'CompanyTable', 'LambdaStack-CompanyTable95A3E353-1PE19ALGDRL0N');
    table.grantReadWriteData(iamRoleForSa);

    // Apart from the permission to access the S3 bucket above, you can also grant permissions of other AWS resources created in this CDK app to such AWS IAM role.
    // Then in the follow-up CDK8S Chart, we will create a K8S Service Account to associate with this AWS IAM role and a nginx K8S deployment to use the K8S SA.
    // As a result, the nginx Pod will have the fine-tuned AWS permissions defined in this AWS IAM role.

    // Now create a Fargate Profile to host customer app which hosting Pods belonging to nginx namespace.
    const customerAppFargateProfile = cluster.addFargateProfile(
      'FargateProfile',
      {
        selectors: [{ namespace: appNamespace }],
        subnetSelection: { subnetType: ec2.SubnetType.PRIVATE },
        vpc: cluster.vpc,
      }
    );
  }
}
import * as cdk from '@aws-cdk/core';
import * as eks from '@aws-cdk/aws-eks';
import * as iam from '@aws-cdk/aws-iam';

export interface AwsLoadBalancerControllerProps {
  eksCluster: eks.ICluster;
}

interface HelmValues {
  [key: string]: unknown;
}

export class AwsLoadBalancerController extends cdk.Construct {

  constructor(scope: cdk.Construct, id: string, props: AwsLoadBalancerControllerProps) {
    super(scope, id);

    const awsLbControllerServiceAccount = props.eksCluster.addServiceAccount(
      'aws-load-balancer-controller',
      {
        name: 'aws-load-balancer-controller',
        namespace: 'kube-system',
      }
    );

    const lbAcmPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'acm:DescribeCertificate',
        'acm:ListCertificates',
        'acm:GetCertificate',
      ],
      resources: ['*'],
    });

    const lbEc2PolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'ec2:AuthorizeSecurityGroupIngress',
        'ec2:CreateSecurityGroup',
        'ec2:CreateTags',
        'ec2:DeleteTags',
        'ec2:DeleteSecurityGroup',
        'ec2:DescribeAccountAttributes',
        'ec2:DescribeAddresses',
        'ec2:DescribeInstances',
        'ec2:DescribeInstanceStatus',
        'ec2:DescribeInternetGateways',
        'ec2:DescribeNetworkInterfaces',
        'ec2:DescribeSecurityGroups',
        'ec2:DescribeSubnets',
        'ec2:DescribeTags',
        'ec2:DescribeVpcs',
        'ec2:ModifyInstanceAttribute',
        'ec2:ModifyNetworkInterfaceAttribute',
        'ec2:RevokeSecurityGroupIngress',
        'ec2:DescribeAvailabilityZones'
      ],
      resources: ['*'],
    });

    const lbElbPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'elasticloadbalancing:AddListenerCertificates',
        'elasticloadbalancing:AddTags',
        'elasticloadbalancing:CreateListener',
        'elasticloadbalancing:CreateLoadBalancer',
        'elasticloadbalancing:CreateRule',
        'elasticloadbalancing:CreateTargetGroup',
        'elasticloadbalancing:DeleteListener',
        'elasticloadbalancing:DeleteLoadBalancer',
        'elasticloadbalancing:DeleteRule',
        'elasticloadbalancing:DeleteTargetGroup',
        'elasticloadbalancing:DeregisterTargets',
        'elasticloadbalancing:DescribeListenerCertificates',
        'elasticloadbalancing:DescribeListeners',
        'elasticloadbalancing:DescribeLoadBalancers',
        'elasticloadbalancing:DescribeLoadBalancerAttributes',
        'elasticloadbalancing:DescribeRules',
        'elasticloadbalancing:DescribeSSLPolicies',
        'elasticloadbalancing:DescribeTags',
        'elasticloadbalancing:DescribeTargetGroups',
        'elasticloadbalancing:DescribeTargetGroupAttributes',
        'elasticloadbalancing:DescribeTargetHealth',
        'elasticloadbalancing:ModifyListener',
        'elasticloadbalancing:ModifyLoadBalancerAttributes',
        'elasticloadbalancing:ModifyRule',
        'elasticloadbalancing:ModifyTargetGroup',
        'elasticloadbalancing:ModifyTargetGroupAttributes',
        'elasticloadbalancing:RegisterTargets',
        'elasticloadbalancing:RemoveListenerCertificates',
        'elasticloadbalancing:RemoveTags',
        'elasticloadbalancing:SetIpAddressType',
        'elasticloadbalancing:SetSecurityGroups',
        'elasticloadbalancing:SetSubnets',
        'elasticloadbalancing:SetWebAcl',
      ],
      resources: ['*'],
    });

    const lbIamPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'iam:CreateServiceLinkedRole',
        'iam:GetServerCertificate',
        'iam:ListServerCertificates',
      ],
      resources: ['*'],
    });

    const lbCognitoPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: ['cognito-idp:DescribeUserPoolClient'],
      resources: ['*'],
    });

    const lbWafRegPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'waf-regional:GetWebACLForResource',
        'waf-regional:GetWebACL',
        'waf-regional:AssociateWebACL',
        'waf-regional:DisassociateWebACL',
      ],
      resources: ['*'],
    });

    const lbTagPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: ['tag:GetResources', 'tag:TagResources'],
      resources: ['*'],
    });

    const lbWafPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: ['waf:GetWebACL'],
      resources: ['*'],
    });

    const lbWafv2PolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'wafv2:GetWebACL',
        'wafv2:GetWebACLForResource',
        'wafv2:AssociateWebACL',
        'wafv2:DisassociateWebACL',
      ],
      resources: ['*'],
    });

    const lbShieldPolicyStatements = new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      actions: [
        'shield:DescribeProtection',
        'shield:GetSubscriptionState',
        'shield:DeleteProtection',
        'shield:CreateProtection',
        'shield:DescribeSubscription',
        'shield:ListProtections',
      ],
      resources: ['*'],
    });

    awsLbControllerServiceAccount.addToPrincipalPolicy(lbAcmPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbEc2PolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbElbPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbIamPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbCognitoPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbWafRegPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbTagPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbWafPolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbWafv2PolicyStatements);
    awsLbControllerServiceAccount.addToPrincipalPolicy(lbShieldPolicyStatements);

    // Deploy AWS LoadBalancer Controller from the Helm chart
    const stack = cdk.Stack.of(this);
    const lbHelmValues = {} as HelmValues;
    lbHelmValues.clusterName = props.eksCluster.clusterName;
    lbHelmValues.region = stack.region;
    lbHelmValues.vpcId = props.eksCluster.vpc.vpcId;
    lbHelmValues.serviceAccount = {
      create: false,
      name: 'aws-load-balancer-controller',
    };

    props.eksCluster.addHelmChart('aws-load-balancer-controller', {
      chart: 'aws-load-balancer-controller',
      repository: 'https://aws.github.io/eks-charts',
      namespace: 'kube-system',
      values: lbHelmValues,
    });
  }
}
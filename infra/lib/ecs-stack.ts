import * as cdk from '@aws-cdk/core';
import * as ec2 from '@aws-cdk/aws-ec2';
import * as autoscaling from '@aws-cdk/aws-autoscaling';
import * as ecs from '@aws-cdk/aws-ecs';
import * as dynamodb from '@aws-cdk/aws-dynamodb';
import * as logs from '@aws-cdk/aws-logs';
import * as elbv2 from '@aws-cdk/aws-elasticloadbalancingv2';
import * as ecr from '@aws-cdk/aws-ecr';
import * as ecsPatterns from '@aws-cdk/aws-ecs-patterns';

export class EcsStack extends cdk.Stack {

  constructor(scope: cdk.App, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // const vpc = new ec2.Vpc(this, 'EcsVpc', {
    //   maxAzs: 2,
    //   natGateways: 1,
    // });
    const vpc = ec2.Vpc.fromLookup(this, 'Vpc', {
      isDefault: true
    });

    const cluster = new ecs.Cluster(this, 'EcsCluster', {
      vpc: vpc,
    });

    const autoScalingGroup = new autoscaling.AutoScalingGroup(this, 'Asg', {
      vpc,
      instanceType: new ec2.InstanceType('t2.micro'),
      machineImage: ecs.EcsOptimizedImage.amazonLinux2(),
      minCapacity: 1,
      maxCapacity: 1,
    });

    const capacityProvider = new ecs.AsgCapacityProvider(this, 'AsgCapacityProvider', {
      autoScalingGroup,
      // The name cannot be prefixed with "aws", "ecs", or "fargate".
      //capacityProviderName: 'asg-provider'
      // to fix bug: Can't delete a stack with ASG Capacity providers
      enableManagedTerminationProtection: false
    });

    cluster.addAsgCapacityProvider(capacityProvider);

    const repository = ecr.Repository.fromRepositoryName(this, 'DemoAppRepo', 'demo-app');

    const table = dynamodb.Table.fromTableName(this, 'CompanyTable', 'LambdaStack-CompanyTable95A3E353-1PE19ALGDRL0N');

    const loadBalancedEcsService = new ecsPatterns.ApplicationLoadBalancedEc2Service(this, 'DemoEcsSvc', {
      cluster,
      publicLoadBalancer: true,
      desiredCount: 1,
      memoryLimitMiB: 256,
      cpu: 256,
      taskImageOptions: {
        image: ecs.ContainerImage.fromEcrRepository(repository),
        //containerPort: 8080,
        environment: {
          COMPANY_TABLE_NAME: table.tableName,
        },
      },
    });

    loadBalancedEcsService.targetGroup.configureHealthCheck({
      path: '/actuator/health/liveness',
      timeout: cdk.Duration.seconds(20),
    });

    table.grantReadWriteData(loadBalancedEcsService.taskDefinition.taskRole);

    // const taskDefinition = new ecs.Ec2TaskDefinition(this, 'TaskDef');

    // table.grantReadWriteData(taskDefinition.taskRole);

    // const logging = new ecs.AwsLogDriver({
    //   streamPrefix: 'EcsDemo',
    //   logRetention: logs.RetentionDays.ONE_WEEK,
    // });

    // const demoContainer = taskDefinition.addContainer('DemoContainer', {
    //   image: ecs.ContainerImage.fromEcrRepository(repository),
    //   memoryLimitMiB: 256,
    //   logging,
    //   environment: {
    //     'COMPANY_TABLE_NAME': table.tableName,
    //   },
    // });

    // demoContainer.addPortMappings({
    //   containerPort: 8080,
    //   protocol: ecs.Protocol.TCP,
    // });

    // const ecsService = new ecs.Ec2Service(this, 'DemoService', {
    //   cluster,
    //   taskDefinition,
    //   // cloudMapOptions: {
    //   //   containerPort: 8080
    //   // },
    //   capacityProviderStrategies: [
    //     {
    //       capacityProvider: capacityProvider.capacityProviderName,
    //       weight: 1,
    //     }
    //   ],
    // });

    // const lb = new elbv2.ApplicationLoadBalancer(this, 'LB', { vpc, internetFacing: true });

    // const listener = lb.addListener('Listener', { port: 80 });

    // const targetGroup = listener.addTargets('DemoTG', {
    //   port: 80,
    //   targets: [ecsService.loadBalancerTarget({
    //     containerName: 'DemoContainer',
    //     containerPort: 8080
    //   })]
    // });

    // targetGroup.configureHealthCheck({
    //   path: '/actuator/health/liveness',
    //   protocol: elbv2.Protocol.HTTP,
    // });
  }
}
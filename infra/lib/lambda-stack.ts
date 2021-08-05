import * as cdk from '@aws-cdk/core';
import * as s3n from '@aws-cdk/aws-s3-notifications';
import * as lambda from '@aws-cdk/aws-lambda';
import * as s3 from '@aws-cdk/aws-s3';
import * as dynamodb from '@aws-cdk/aws-dynamodb';

export class LambdaStack extends cdk.Stack {

  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const handler = new lambda.Function(this, 'UploadCompanyDataFunction', {
      runtime: lambda.Runtime.JAVA_11,
      handler: 'idv.demo.App::handleRequest',
      code: lambda.Code.fromAsset('../lambda/upload-company-data/target/uploadcompanydata.jar'),
      memorySize: 512,
      timeout: cdk.Duration.seconds(10)
    });

    // 👇 create bucket
    const bucket = new s3.Bucket(this, 'CompanyDataBucket', {
      //autoDeleteObjects: true,
      removalPolicy: cdk.RemovalPolicy.DESTROY,
    });

    bucket.grantRead(handler);

    // 👇 invoke lambda every time an object is created in the bucket
    bucket.addEventNotification(
      s3.EventType.OBJECT_CREATED,
      new s3n.LambdaDestination(handler),
      {prefix: 'company/', suffix: '.csv'},
    );

    const table = new dynamodb.Table(this, 'CompanyTable', {
      partitionKey: { name: 'name', type: dynamodb.AttributeType.STRING },
      sortKey: {name: 'address1', type: dynamodb.AttributeType.STRING},
      billingMode: dynamodb.BillingMode.PAY_PER_REQUEST,
      removalPolicy: cdk.RemovalPolicy.DESTROY
    });

    table.grantReadWriteData(handler);

    handler.addEnvironment('TABLE_NAME', table.tableName);

    new cdk.CfnOutput(this, 'BucketName', {
      value: bucket.bucketName,
    });

    new cdk.CfnOutput(this, 'TableName', {
      value: table.tableName,
    });
  }
}


/**
 * This action will write to Cloud Object Storage.  If the Cloud Object Storage
 * service is not bound to this action or to the package containing this action,
 * then you must provide the service information as argument input to this function.
 * Cloud Functions actions accept a single parameter, which must be a JSON object.
 *
 * In this case, the args variable will look like:
 *   {
 *     "bucket": "your COS bucket name",
 *     "key": "Name of the object to write",
 *     "body": "Body of the object to write"
 *   }
 */
const CloudObjectStorage = require('ibm-cos-sdk');

async function main(args) {
  const { cos, params } = getParamsCOS(args, CloudObjectStorage);
  let response;
  let key = getID();
  
  const result = {
    bucket: "witpixies",
    key: params.key,
  };

if (!params.body || !cos) {
    result.message = "bucket name, key, body, and apikey are required for this operation.";
    throw result;
  }

  try {
    response = await cos.putObject({
      Bucket: params.bucket, Key: params.key, Body: 'params.body',
    }).promise();
  } catch (err) {
    console.log(err);
    result.message = err.message;
    throw result;
  }
  response.objURL = 'xxxxxx/'+params.key;

  return {"data" : response};
}

// create object storage to put object
function getParamsCOS(args, COS) {
  let { body, key } = args;
  if (body.type === 'Buffer') {
    body = Buffer.from(body.data);
  }

  const ibmAuthEndpoint = 'xxxxx';
  const apiKeyId ='xxxxxx';
  const endpoint= 'xxxxx';
  const serviceInstanceId = 'xxx3xxxxs';
  const params = {};
  params.bucket = 'witpixies';
  params.key = key;
  params.body = body;

  if (!apiKeyId) {
    const cos = null;
    return { cos, params };
  }

  const cos = new COS.S3({
    endpoint, ibmAuthEndpoint, apiKeyId, serviceInstanceId,
  });
 
  return { cos, params };
}
// Generate a random key 
function getID() {
  return '_' + Math.random().toString(36).substr(2, 9);
};

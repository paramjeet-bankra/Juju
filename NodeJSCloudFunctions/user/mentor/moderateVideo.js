/**
  *
  * main() will be run when you invoke this action
  *
  * @param Cloud Functions actions accept a single parameter, which must be a JSON object.
  *
  * @return The output of this action, which must be a JSON object.
  *
  */
 const { Client } = require('pg')

 function main(params) {
     if(params.videoID === undefined) {
          return Promise.reject({ error: 'videoID is missing'});
     }
      let querString ='UPDATE video SET status ='+params.status+
      'where video_id = '+params.videoID;
     
     const client = new Client({
       user: 'xxxxx',
       host: 'echo.db.elephantsql.com',
       database: 'xxxxx',
       password: 'xxx-l7Sv4gxxxQek3Uf4',
       port: 5432,
     })
     
     return client.connect()
     .then(() => client.query(querString))
     .then(res => myres=res)
     .then(() => client.end())
     .then(() => {
         
         return {"data" : { status: 'ok'}} }) 
     .catch(e => {client.end(); return {"error": e}})
 }
 
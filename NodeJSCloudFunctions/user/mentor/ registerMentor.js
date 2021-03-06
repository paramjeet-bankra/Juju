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
 const crypto = require('crypto');
 
  function getHashPass(password) {
     const passwordSha1 = crypto
     .createHash('sha1')
     .update(password)
     .digest('hex');

   return crypto
     .createHash('md5')
     .update(passwordSha1)
     .digest('hex')
 }

function main(params) {
    if(params.name === undefined || params.password === undefined || params.emailID === undefined || params.schoolID === undefined) {
        return Promise.reject({ error: 'params missing'});
   }
   
   let hashPass;
   
   // get hash of password
   if(params.password){
       hashPass = getHashPass(params.password);
   }
   
    let querString ='INSERT INTO mentors(name, password, email_id, school_id)'+
    'VALUES('+params.name+',\''+hashPass+'\','+params.emailID+','+ params.schoolID+')'
   
   const client = new Client({
     user: 'xxxx',
     host: 'echo.db.elephantsql.com',
     database: 'xxxx',
     password: 'xxxx-xxxx',
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

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

 async function main(args) {
     if(args.videoURL || args.thumbnailURL ||args.desc||args.title || args.studentID ||args.categoryID||args.mentorID || args.ageGroup){
         return Promise.reject({ "error": "params missing"});
     }
    //query to insert video link to db
     const queryString ='INSERT INTO video(title, link, description, thumbnail, status, student_id, category_id, mentor_id, age_group)' 
     +' VALUES(\''+args.title+'\',\''+args.videoURL+'\',\''+args.desc+'\',\''+args.thumbnailURL+'\',\'pending\','+args.studentID+','+args.categoryID+','+args.mentorID+','+args.ageGroup+')';
   
   const client = new Client({
       user: 'xxxx',
       host: 'echo.db.elephantsql.com',
       database: 'xxxx',
       password: 'xxxx-xxxx',
       port: 5432,
     })
     return client.connect()
     .then(() => client.query(queryString))
     .then(res => myres=res)
     .then(() => client.end())
     .then(() => {
         
         
         return {"data" : { status: 'ok' }} }) 
     .catch(e => {client.end(); return {"error": "Something occured"}})
 }
 
 
 
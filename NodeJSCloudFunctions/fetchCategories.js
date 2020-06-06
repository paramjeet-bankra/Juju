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
     const client = new Client({
       user: 'xxxx',
       host: 'echo.db.elephantsql.com',
       database: 'xxxx',
       password: 'xxxx',
       port: 5432,
     })
     return client.connect()
     .then(() => client.query('SELECT * FROM categories'))
     .then(res => myres=res)
     .then(() => client.end())
     .then(() => {
         
         return {"data" : myres.rows} }) 
     .catch(e => {return {"error": e}})
 }
 
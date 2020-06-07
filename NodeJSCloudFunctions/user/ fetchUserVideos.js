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
    if(params.userID === undefined) {
        return Promise.reject({ error: 'userID missing'});
     }

     // db connection
    const client = new Client({
      user: 'xxxx',
      host: 'echo.db.elephantsql.com',
      database: 'xxxx',
      password: 'xxxx-xxxx',
      port: 5432,
    })
    
   
    let searchStr = '';
    
    if(params.type === 'mentor') {
        searchStr = `WHERE video.mentor_id =${params.userID}`
    }
    else {
        searchStr = `WHERE video.student_id =${params.userID}`
    }
    
    let query = 'SELECT video.*, categories.name as caetgoryName, categories.category_id'+
                    ' FROM video'+
                    ' INNER JOIN categories ON'+
                    ' categories.category_id=video.category_id '+ searchStr;  
                    
    return client.connect()
    .then(() => client.query(query))
    .then(res => myres=res)
    .then(() => client.end())
    .then(() => {
        return {"data" : myres.rows} }) 
    .catch(e => {return {"error": "Something occured"}})
}


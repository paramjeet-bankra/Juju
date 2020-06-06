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
 
 function modifyRes(myres){
     let data = [];
     
     myres.forEach(obj => {
         const findID = data.findIndex(arr => arr.id === obj.category_id);
        
         if(findID === -1) {
             let { category_id, name, imageurl }= obj;
              delete obj.category_id;
             delete obj.name;
             delete obj.imageurl;
             data.push({
                 name: name,
                 id: category_id,
                 imageurl: imageurl,
                 videos: [ {...obj} ]
             })
         }
         else {
             delete obj.category_id;
             delete obj.name;
             delete obj.imageurl;
             data[findID].videos.push(obj)
         }
     });
     return data;
 }

function main(params) {
    const client = new Client({
      user: 'xxxx',
      host: 'echo.db.elephantsql.com',
      database: 'xxxx',
      password: 'xxx-xxxk3Uf4',
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
        let res = modifyRes(myres.rows);
        return {"data" : res} }) 
    .catch(e => {return {"error": e}})
}


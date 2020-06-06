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
      user: 'zaalvgrj',
      host: 'echo.db.elephantsql.com',
      database: 'zaalvgrj',
      password: 'bh2MTutGeSKL4f3M_h-l7Sv4gQek3Uf4',
      port: 5432,
    })
    
    let searchStr = '';
    if( params.categoryID) {
        searchStr = `WHERE categories.category_id IN (${params.categories})`;
    }
    let query = 'SELECT categories.*, video.*, mt.name as mentorName, mt.mentor_id, st.name as studentName, st.student_id'+
                    ' FROM categories'+
                    ' INNER JOIN video ON'+
                    ' categories.category_id=video.category_id'+
                    ' INNER JOIN student st ON video.student_id = st.student_id'+
                    ' INNER JOIN mentors mt ON video.mentor_id = mt.mentor_id '+ searchStr;
               console.log(query);     
                    
    return client.connect()
    .then(() => client.query(query))
    .then(res => myres=res)
    .then(() => client.end())
    .then(() => {
        let res = modifyRes(myres.rows);
        return {"data" : res} }) 
    .catch(e => { client.end(); return {"error": e}})
}


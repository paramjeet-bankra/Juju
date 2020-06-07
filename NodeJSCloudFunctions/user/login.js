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
    if(params.name === undefined || params.password === undefined) {
        return Promise.reject({ "error": 'params missing'});
    }
    
    let hashPass;
    
    //get hash of password
    if(params.password){
        hashPass = getHashPass(params.password);
    }
    
    const client = new Client({
      user: 'zaalvgrj',
      host: 'echo.db.elephantsql.com',
      database: 'zaalvgrj',
      password: 'bh2MTutGeSKL4f3M_h-l7Sv4gQek3Uf4',
      port: 5432,
    })
    
    if(params.name) {
        let querString;
        
        if(params.type === 'student') {
            querString ='SELECT st.*, ment.name as mentorName, ment.mentor_id, sch.name as schoolName, sch.school_id FROM student st'+ 
            ' JOIN school sch ON st.school_id=sch.school_id'+ 
            ' JOIN mentors ment ON st.mentor_id=ment.mentor_id where st.name =\''+params.name+'\'';
        }
        else {
             querString ='SELECT ment.*, sch.name as schoolName, sch.school_id FROM mentors ment'+ 
            ' JOIN school sch ON ment.school_id=sch.school_id'+ 
            ' where ment.name =\''+params.name+'\'';
        }
      
        return client.connect()
                .then(() => client.query(querString ))
                .then(res => myres=res)
                .then(() => {
                    if(myres.rows.length === 0) {
                        return { "error": 'User does not exist'};
                    }
                    const user = myres.rows[0];
                    
                    if (user.password === hashPass) {
                        delete user.password;
                         console.log("success");
                       
                        client.end();
                        return {"data" : user} 
                    }
                    else {
                        return { "error": "Password is incorrect"};
                    }
                })
                .catch(e => {client.end(); return {"error": "Something occured"}})
    }
}

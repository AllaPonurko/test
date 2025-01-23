import React, {useState, useEffect} from 'react';

const UserListPage = () =>{
console.log('UserListPage rendered');
const[users,setUsers]=useState([]);
const[error,setError]=useState(null);
const fetchUsers=async()=>{
try{
const response=await fetch('http://localhost:8082/test/api/user/getUsers');
if (!response.ok) {
                      throw new Error('Network response was not ok');
                  }
                const data=await response.json();
                setUsers(data);
         }catch(error){
         setError(error.message);
         console.error('Error fetching users:', error);
         }
     };
   useEffect(()=>{
   fetchUsers();
   },[]) ;
   return(
   <div>
     <h1>Users List</h1>
        {
        error?(
            <p>Error:{error}</p>
              ):(
              <ul>

                 { users.map(user=>
                     (
                        <li key={user.id}>
                           {user.name} - {user.email}
                        </li>
                     )
                )}
              </ul>
        )}
   </div>);
};
export default UserListPage;
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
const HomePage = () => {
console.log('HomePage rendered');
const [userData, setUserData]=useState(null);
const fetchUserData=async()=>{
console.log('Fetching user data...');
const userId ='df3c27ab-d13c-4bc0-9fa8-1faa16d5082f';
try{
const response= await fetch('http://localhost:8080/test/api/user/getUser?id=${userId}');
console.log('Response:', response);
if(!response.ok)
{
throw new Error('Network response was not ok');
}
const data=await response.json();
console.log('Data:', data);
setUserData(data);
}catch(error){
console.error('There was a problem with the fetch operation:', error);
}
};
    return (
        <div>
            <h1>Welcome to My App</h1>
            <button onClick={() => {
                console.log('Button clicked!');
                fetchUserData();
            }}>Fetch User Data</button>
            {userData &&(
            <div>
                     <h2>User Data</h2>

                     <p>Name: {userData.name}</p>
                     <p>Name: {userData.email}</p>
            </div>
            )}
            <div>
                     <Link to="/about">Go to About Page</Link>
            </div>
            <div>
                     <Link to="/userList">Go to Users List</Link>
            </div>
            <div>
                     <Link to="/chat">Go to Chat</Link>
            </div>
        </div>

    );
};

export default HomePage;

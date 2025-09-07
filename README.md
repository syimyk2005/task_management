Долбоорду орнотуу учун биз git clone командасы менен озубузго жуктойбуз.
Код редактору Intellij idea.
Андан сон терминалга кирип docker-compose up --build командасын иштетебиз.
Бизде эки контейнер которулот озубуздун долбоорубуз жана маалыматтык база.

Бул проектте мен техникалык тапшырмадагы ар бир талапты аткарганга аракет кылдым.
Ар бир Эндпоинт техникалык заданияда жазылгандай корголгон.
Авторизацияга тиешелуу эмес эндпоинттердин баарына Postmanдагы 
Authorization деген пункта токенди киргизуу кажет(Токендин туру Bearer Token)
<img width="816" height="497" alt="image" src="https://github.com/user-attachments/assets/eef41294-e18b-40d2-86bf-588c2d88eef8" />

_____________________________________________________
  1. Бул APIлер авторизацияга тиешелуу:
_____________________________________________________
1) API POST http://localhost:8080/api/auth/register)
{
  "username": "syimyk11",
  "email": "john.doe@example.com",
  "firstname": "Jo2hn",
  "lastname": "D2oe",
  "password": "111",
  "role": "USER"
}


2) POST http://localhost:8080/api/auth/login
{
    "username": "syimyk11",
    "password": "111"
}

3) http://localhost:8080/auth/refresh_token

4) http://localhost:8080/auth/logout
   
______________________________________________________
  2.Бул APIлер колдонуучуларды бакшаруу учун
______________________________________________________

1) GET http://localhost:8080/api/users
  
2) GET http://localhost:8080/api/users/1
   
3) PUT http://localhost:8080/api/users/1
{
    "username": "syimyk",
    "email": "john.doe@example2.com",
    "firstname": "Jo2hn",
    "lastname": "D2oe",
    "role": "USER"
}

4) DELETE http://localhost:8080/api/users/1

______________________________________________________
3.Бул APIлер командаларды бакшаруу учун
______________________________________________________
   
1) GET http://localhost:8080/api/teams

2) GET http://localhost:8080/api/teams/1

3) POST http://localhost:8080/api/teams
{
  "name": "Team Alpha",
  "description": "Some description",
  "createdBy": 1
}

4) PUT http://localhost:8080/api/teams/1
{
  "name": "Team Alpha",
  "description": "Some description",
  "createdBy": 552
}

5) DELETE http://localhost:8080/api/teams/1
   
________________________________________________________
  4. Бул APIлер Катышуучуларды бакшаруу учун
________________________________________________________

1) GET http://localhost:8080/api/teams/1/members

2) POST http://localhost:8080/api/teams/1/members
{
    "userId": 552
}

3) DELETE http://localhost:8080/api/teams/1/members/1
   
_________________________________________________________
  5. Бул APIлер тапшырмаларды бакшаруу учун
_________________________________________________________

1) GET http://localhost:8080/api/tasks?page=0&size=10


2) GET http://localhost:8080/api/tasks/1

3) POST http://localhost:8080/api/tasks
{
  "title": "Finish project",
  "description": "Complete the task management module",
  "status": "IN_PROGRESS",
  "priority": 1,
  "category": "Development",
  "createdBy": 1,
  "assignedTo": 1,
  "team": 1,
  "deadline": "2025-09-10T18:00:00"
}

4) DELETE http://localhost:8080/api/tasks/1

5) PUT http://localhost:8080/api/tasks/1
{
  "title": "Finish project",
  "description": "Complete the task management module",
  "status": "IN_PROGRESS",
  "priority": 1,
  "category": "Development",
  "createdBy": 4,
  "assignedTo": 4,
  "team": 1,
  "deadline": "2025-09-10T18:00:00"
}



    
   

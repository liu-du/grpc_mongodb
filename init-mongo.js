db.createUser(
  {
    user : 'jimmy',
    pwd  : 'pass',
    roles: [
      {
        role : 'readWrite',
        db   : 'blog'
      }
    ]
  }
)
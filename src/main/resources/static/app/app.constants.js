angular.module('myApp')

.constant('AUTH_EVENTS', {
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized'
})

.constant('USER_ROLES', {
  admin: 'ADMIN',
  storeOwner: 'CREATOR',
  shipper: 'BRAND'
})

.constant('SERVER', {
  //url: 'http://151.80.45.41:9085'
  url: 'http://localhost:8094'
})

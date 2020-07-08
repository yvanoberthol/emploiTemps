(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Contact', Contact);

      Contact.$inject = ['$http'];

      function Contact ($http) {
           return{
              save: function(contact, onSaveSuccess, onSaveError){
                  return $http.post("/api/contacts", contact)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(contact, onSaveSuccess, onSaveError){
                  return $http.put("/api/contacts", contact).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              getAll: function(page, size, sortBy, direction, name, phone, group){
                  return $http.get("/api/contacts"
                                  +"?page=" + page
                                  + "&size=" + size
                                  + "&sortBy=" + sortBy
                                  + "&direction=" + direction
                                  + "&name=" + name
                                  + "&phone=" + phone
                                  + "&group=" + group);
              },
              get: function(id){
                    return $http.get("/api/contacts/" + id);
              },
              delete: function(id){
                    return $http.delete("/api/contacts/" + id);
              }
           };
      }
})();
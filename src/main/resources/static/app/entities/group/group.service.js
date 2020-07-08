(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Group', Group);

      Group.$inject = ['$http'];

      function Group ($http) {
           return{
                save: function(group, onSaveSuccess, onSaveError){
                    return $http.post("/api/groups", group)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
                },
                update: function(group, onSaveSuccess, onSaveError){
                    return $http.put("/api/groups", group).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
                },
                get: function(id){
                    return $http.get("/api/groups/" + id);
                },
                getAll: function(page, size, sortBy, direction){
                      return $http.get("/api/groups"
                                      +"?page=" + page
                                      + "&size=" + size
                                      + "&sortBy=" + sortBy
                                      + "&direction=" + direction);
                },
                delete: function(groupId){
                    return $http.delete("/api/groups/" + groupId);
                }
           };
      }
})();
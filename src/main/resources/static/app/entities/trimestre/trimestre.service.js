(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Trimestre', Trimestre);

      Trimestre.$inject = ['$http'];

      function Trimestre ($http) {
           return{
              getAll: function(){
                    return $http.get("/api/trimestres");
              },
              getByAnnee: function(anneeId){
                    return $http.get("/api/trimestres-by-annee/" + anneeId);
              },
              save: function(trimestre, onSaveSuccess, onSaveError){
                  return $http.post("/api/trimestres", trimestre)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(trimestre, onSaveSuccess, onSaveError){
                  return $http.put("/api/trimestres", trimestre).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              get: function(id){
                    return $http.get("/api/trimestres/" + id);
              },
              delete: function(id){
                    return $http.delete("/api/trimestres/" + id);
              }
           };
      }
})();
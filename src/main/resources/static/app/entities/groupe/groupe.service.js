(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Groupe', Groupe);

      Groupe.$inject = ['$http', 'Upload'];

      function Groupe ($http, Upload) {
           return{
              getByPromo: function(promoId){
                    return $http.get("/api/groupes-by-promo/" + promoId);
              },
              save: function(groupe, onSaveSuccess, onSaveError){
                  return $http.post("/api/groupes", groupe)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(groupe, onSaveSuccess, onSaveError){
                  return $http.put("/api/groupes", groupe).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              get: function(id){
                    return $http.get("/api/groupes/" + id);
              },
              delete: function(id){
                    return $http.delete("/api/groupes/" + id);
              }
           };
      }
})();
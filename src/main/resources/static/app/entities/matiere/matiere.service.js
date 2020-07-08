(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Matiere', Matiere);

      Matiere.$inject = ['$http'];

      function Matiere ($http) {
           return{
              save: function(matiere, onSaveSuccess, onSaveError){
                  return $http.post("/api/matieres", matiere)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(matiere, onSaveSuccess, onSaveError){
                  return $http.put("/api/matieres", matiere).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              getByPromo: function(promoId){
                  return $http.get("/api/matieres-by-promo/" + promoId);
              },
              get: function(id){
                    return $http.get("/api/matieres/" + id);
              },
              delete: function(id){
                    return $http.delete("/api/matieres/" + id);
              }
           };
      }
})();
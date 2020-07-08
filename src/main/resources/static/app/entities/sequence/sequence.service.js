(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Sequence', Sequence);

      Sequence.$inject = ['$http'];

      function Sequence ($http) {
           return{
              query: function(){
                        return $http.get("/api/sequences");
              },
              save: function(sequence, onSaveSuccess, onSaveError){
                  return $http.post("/api/sequences", sequence)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(sequence, onSaveSuccess, onSaveError){
                  return $http.put("/api/sequences", sequence).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              getAll: function(trimestreId){
                  return $http.get("/api/sequences-by-trimestre/" + trimestreId);
              },
              getForPromoMatiere: function(promoId, matiereId){
                  return $http.get("/api/sequences-for-promo-matiere?promoId=" + promoId + "&matiereId=" + matiereId);
              },
              get: function(id){
                    return $http.get("/api/sequences/" + id);
              },
              delete: function(id){
                    return $http.delete("/api/sequences/" + id);
              }
           };
      }
})();
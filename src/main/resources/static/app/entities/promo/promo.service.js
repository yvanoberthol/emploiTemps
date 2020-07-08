(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Promo', Promo);

      Promo.$inject = ['$http'];

      function Promo ($http) {
           return{
                save: function(promo, onSaveSuccess, onSaveError){
                    return $http.post("/api/promos", promo)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
                },
                update: function(promo, onSaveSuccess, onSaveError){
                    return $http.put("/api/promos", promo).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
                },
                get: function(id){
                    return $http.get("/api/promos/" + id);
                },
                getByAnnee: function(anneeId){
                    return $http.get("/api/promos-by-annee/" + anneeId)
                },
                delete: function(promoId){
                    return $http.delete("/api/promos/" + promoId);
                }
           };
      }
})();
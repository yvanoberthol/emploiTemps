(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('PromoMatiere', PromoMatiere);

      PromoMatiere.$inject = ['$http'];

      function PromoMatiere ($http) {
           return{
              update: function(promoMatiere, onSaveSuccess, onSaveError){
                  return $http.put("/api/promo-matieres", promoMatiere).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              getByPromo: function(promoId){
                  return $http.get("/api/promo-matieres-by-promo/" + promoId)
              },
              get: function(promoId, matiereId){
                    return $http.get("/api/promo-matiere?promoId=" + promoId + "&matiereId=" + matiereId)
              },
           };
      }
})();
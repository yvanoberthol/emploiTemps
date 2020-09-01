(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Promo', Promo);

      Promo.$inject = ['$http', 'Upload'];

      function Promo ($http, Upload) {
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
                importNotes: function(file, promoId, sequenceId, matiereId, onSuccess, onError){
                     return  Upload.upload({
                         url: "/api/promo-import-notes?promo=" + promoId + "&sequence=" + sequenceId+ "&matiere=" + matiereId,
                         data: {file: file }
                     }).then(function(response){
                         onSuccess(response);
                       }, function errorCallback(response) {
                        onError(response);
                    });
                },
                delete: function(promoId){
                    return $http.delete("/api/promos/" + promoId);
                }
           };
      }
})();
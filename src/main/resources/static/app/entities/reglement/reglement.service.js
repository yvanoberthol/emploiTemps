(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Reglement', Reglement);

      Reglement.$inject = ['$http', 'Upload'];

      function Reglement ($http, Upload) {
           return{
              query: function(){
                    return $http.get("/api/reglements");
              },
              getAll: function(page, size, sortBy, direction, createdDateFrom, createdDateTo){
                        return $http.get("/api/reglements"
                                            +"?page=" + page
                                            + "&size=" + size
                                            + "&sortBy=" + sortBy
                                            + "&direction=" + direction
                                            + "&createdDateFrom=" + createdDateFrom
                                            + "&createdDateTo=" + createdDateTo);
              },
              getByAnnee: function(page, size, sortBy, direction, createdDateFrom, createdDateTo, anneeId, promoId){
                      return $http.get("/api/reglements-by-annee/" + anneeId
                                          +"?page=" + page
                                          + "&size=" + size
                                          + "&sortBy=" + sortBy
                                          + "&direction=" + direction
                                          + "&createdDateFrom=" + createdDateFrom
                                          + "&createdDateTo=" + createdDateTo
                                          + "&promo=" + promoId);
              },
              getByStudent: function(page, size, sortBy, direction, createdDateFrom, createdDateTo, studentId){
                    return $http.get("/api/reglements-by-student/" + studentId
                                        +"?page=" + page
                                        + "&size=" + size
                                        + "&sortBy=" + sortBy
                                        + "&direction=" + direction
                                        + "&createdDateFrom=" + createdDateFrom
                                        + "&createdDateTo=" + createdDateTo);
              },
              save: function(reglement, file, onSaveSuccess, onSaveError){
                   return  Upload.upload({
                       url: '/api/reglements',
                       data: {file: file, 'reglement': Upload.jsonBlob(reglement) }
                   }).then(function(response){
                       onSaveSuccess(response);
                     }, function errorCallback(response) {
                      onSaveError(response);
                     });
              },
              update: function(reglement, file, onSaveSuccess, onSaveError){
                  return  Upload.upload({
                       url: '/api/reglements',
                        method: 'PUT',
                       data: {file: file, 'reglement': Upload.jsonBlob(reglement) }
                  })
                  .then(function(response){
                       onSaveSuccess(response);
                     },
                     function errorCallback(response) {
                      onSaveError(response);
                  });
              },
              get: function(id){
                    return $http.get("/api/reglements/" + id);
              },
              getSexes: function(){
               return $http.get("/api/sexes");
              },
              getJustificatif: function(reglementId){
                  return $http.get("/api/reglement-justificatif/" + reglementId, {responseType: "arraybuffer"})
              },
              delete: function(id){
                    return $http.delete("/api/reglements/" + id);
              }
           };
      }
})();
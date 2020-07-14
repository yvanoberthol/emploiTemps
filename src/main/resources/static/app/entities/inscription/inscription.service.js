(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Inscription', Inscription);

      Inscription.$inject = ['$http', 'Upload'];

      function Inscription ($http, Upload) {
           return{
              query: function(){
                    return $http.get("/api/inscriptions");
              },
              getByAnnee: function(page, size, sortBy, direction, anneeId, name, promoId){
                                  return $http.get("/api/inscriptions-by-annee/" + anneeId
                                          +"?page=" + page
                                          + "&size=" + size
                                          + "&sortBy=" + sortBy
                                          + "&direction=" + direction
                                          + "&name=" + name
                                          + "&promo=" + promoId);
              },
              /*getByPromo: function(page, size, sortBy, direction, promoId){
                  return $http.get("/api/inscriptions-by-promo/" + promoId
                                  +"?page=" + page
                                  + "&size=" + size
                                  + "&sortBy=" + sortBy
                                  + "&direction=" + direction);
              },*/
              getByPromo: function(promoId, name){
                          return $http.get("/api/inscriptions-by-promo/" + promoId + "?name=" + name);
              },
              /*getByPromoWithAttendances: function(page, size, sortBy, direction, promoId, edtEventId){
                  return $http.get("/api/inscriptions-by-promo-with-attendances/" + promoId
                                  +"?page=" + page
                                  + "&size=" + size
                                  + "&sortBy=" + sortBy
                                  + "&direction=" + direction
                                  + "&edtEventId=" + edtEventId);
              },*/
              getByDay: function(createdDateFrom, createdDateTo){
                        return $http.get("/api/inscriptions-by-day"
                                            +"?createdDateFrom=" + createdDateFrom
                                            + "&createdDateTo=" + createdDateTo);
              },
              save: function(inscription, onSaveSuccess, onSaveError){
                  return $http.post("/api/inscriptions", inscription)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              /*update: function(inscription, onSaveSuccess, onSaveError){
                  return $http.put("/api/inscriptions", inscription).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },*/
              update: function(inscription, file, onSaveSuccess, onSaveError){
                    return  Upload.upload({
                         url: '/api/inscriptions',
                          method: 'PUT',
                         data: {file: file, 'inscription': Upload.jsonBlob(inscription) }
                    })
                    .then(function(response){
                         onSaveSuccess(response);
                       },
                       function errorCallback(response) {
                        onSaveError(response);
                    });
              },
              get: function(studentId, promoId){
                    return $http.get("/api/inscriptions?studentId=" + studentId + "&promoId=" + promoId);
              },
              getImage: function(studentId, promoId){
                  return $http.get("/api/inscription-image?studentId=" + studentId + "&promoId=" + promoId, {responseType: "arraybuffer"})
              },
              getSexes: function(){
               return $http.get("/api/sexes");
              },
              delete: function(studentId, promoId){
                    return $http.delete("/api/inscriptions?studentId=" + studentId + "&promoId=" + promoId);
              }
           };
      }
})();
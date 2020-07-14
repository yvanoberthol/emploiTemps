(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Student', Student);

      Student.$inject = ['$http', 'Upload'];

      function Student ($http, Upload) {
           return{
              query: function(){
                    return $http.get("/api/students");
              },
              getByAnnee: function(page, size, sortBy, direction, anneeId, name, promoId){
                        return $http.get("/api/students-by-annee/" + anneeId
                                            +"?page=" + page
                                            + "&size=" + size
                                            + "&sortBy=" + sortBy
                                            + "&direction=" + direction
                                            + "&name=" + name
                                            + "&promo=" + promoId);
              },
              getByPromo: function(promoId, name){
                        return $http.get("/api/students-by-promo/" + promoId
                                                            + "?name=" + name);
              },
              getByPromoAndTrimestreIdWithNotes: function(name, promoId, trimestreId){
                    var idTrimestre = 0;
                    if(trimestreId) idTrimestre = trimestreId;
                    return $http.get("/api/students-by-promo-with-notes/" + promoId
                                                  + "?name=" + name
                                                  + "&trimestreId=" + idTrimestre);
              },
              getByPromoWithNotesForMatiereAndTrimestre: function(page, size, sortBy, direction, name, promoId, matiereId, trimestreId){
                        var idMatiere = 0;
                        var idTrimestre = 0;

                        if(matiereId)
                            idMatiere = matiereId;

                        if(trimestreId)
                            idTrimestre = trimestreId;
                        return $http.get("/api/students-by-promo-with-notes-for-matiere-and-trimestre/" + promoId
                                                            +"?page=" + page
                                                            + "&size=" + size
                                                            + "&sortBy=" + sortBy
                                                            + "&direction=" + direction
                                                            + "&matiereId=" + idMatiere
                                                            + "&trimestreId=" + idTrimestre
                                                            + "&name=" + name);
              },
              save: function(student, onSaveSuccess, onSaveError){
                  return $http.post("/api/students", student)
                          .then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              update: function(student, onSaveSuccess, onSaveError){
                  return $http.put("/api/students", student).
                          then(function(response){
                            onSaveSuccess(response);
                          }, function errorCallback(response) {
                           onSaveError(response);
                          });
              },
              saveNote: function(studentNote){
                    return $http.post("/api/students-save-note", studentNote);
              },
              get: function(id){
                    return $http.get("/api/students/" + id);
              },
              getOneByAnnee: function(id, anneeId){
                  return $http.get("/api/student-by-annee/" + id + "?anneeId=" + anneeId);
              },
              getAllByAnnee: function(anneeId, promoId){
                    return $http.get("/api/students-by-annee?anneeId=" + anneeId + "&promoId=" + promoId);
              },
              delete: function(id){
                    return $http.delete("/api/students/" + id);
              }
           };
      }
})();
(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoNotesController', PromoNotesController);

    PromoNotesController.$inject = ['$scope', '$state', '$stateParams', 'session', 'previousState', 'entity',
                                    'Student', 'NgTableParams', 'Trimestre', '$filter'];

    function PromoNotesController($scope, $state, $stateParams, session, previousState, entity,
                                    Student, NgTableParams, Trimestre, $filter) {
        var vm = this;
        vm.promo = entity;

        console.log($stateParams)
        vm.previousState = previousState.name;
        vm.name = "";
        vm.matiereId = $stateParams.matiereId;
        vm.trimestres = [];

        Trimestre.getByAnnee(session.getAnneeId())
        .then(function(response){
            vm.trimestres = response.data;
            vm.trimestreId = $stateParams.trimestreId;
            vm.sequenceId = $stateParams.sequenceId;
            vm.trimestreChanged();
            console.log(vm.trimestres);
        })

        var initialParams = {
            count: 200, // initial page size
            sorting: { 'lastName': "asc" }
        };

        vm.tableParams = new NgTableParams(initialParams, {
            counts: [],
            paginationMaxBlocks: 10,
            paginationMinBlocks: 2,

            getData: function(params) {
                if(params.sorting()){
                    for(var sortBy in params.sorting()){
                        vm.sortBy = sortBy;
                        vm.direction = params.sorting()[sortBy];
                    }
                }

                return Student.getByPromoWithNotesForMatiereAndTrimestre(params.page()-1, params.count(), vm.sortBy, vm.direction, vm.name, vm.promo.id, vm.matiereId, vm.trimestreId)
                .then(function(response){
                    params.total(response.data.totalElements);
                    vm.students = response.data.content;
                    vm.loading = false;
                    return vm.students;
                })
            }
        });

        vm.matiereChanged = function(){
            console.log(vm.matiereId)
            vm.matiere =  $filter('filter')(vm.promo.matieres, {id: vm.matiereId})[0];
            vm.tableParams.reload();
            //console.log(vm.matiere);
        }

        vm.trimestreChanged = function(){
            vm.trimestre =  $filter('filter')(vm.trimestres, {id: vm.trimestreId})[0];
            //console.log(vm.trimestre)
            vm.tableParams.reload();
        }

        vm.saveNote = function(student, sequenceId){
            var studentNote = { studentId: student.id, promoId: vm.promo.id, matiereId: vm.matiereId,
                                sequenceId:sequenceId, note: student.notes[sequenceId] };

            Student.saveNote(studentNote);
        }

        vm.saveNotes = function (student) {
          angular.forEach(student.notes, function(note, sequenceId){
                var studentNote = { studentId: student.id, promoId: vm.promo.id, matiereId: vm.matiereId,
                                    sequenceId:sequenceId, note: student.notes[sequenceId] };
                console.log(studentNote)
                Student.saveNote(studentNote);
          });
          student.edit = !student.edit
        }
    }
})();

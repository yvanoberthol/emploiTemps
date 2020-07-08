(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeDetailMatieresDialogController', AnneeDetailMatieresDialogController);

    AnneeDetailMatieresDialogController.$inject = ['$timeout', '$scope', '$state', '$stateParams', '$uibModalInstance', 'entity', 'Promo',
                                            'Matiere', 'NgTableParams', 'PromoMatiere', '$uibModal', 'Teacher',
                                            'NoteInter', 'PromoMatiereNoteInter'];

    function AnneeDetailMatieresDialogController ($timeout, $scope, $state, $stateParams, $uibModalInstance, entity, Promo,
                                        Matiere, NgTableParams, PromoMatiere, $uibModal, Teacher,
                                        NoteInter, PromoMatiereNoteInter) {
        var vm = this;
        vm.promo = entity;
        console.log(vm.promo);
        vm.loading = true;

        vm.clear = clear;
        vm.save = save;
        vm.savePromoMatiere = savePromoMatiere;

        function clear () {
             $uibModalInstance.dismiss();
         }

        function save () {
            vm.isSaving = true;
            if (vm.promo.id !== null) {
                Promo.update(vm.promo, onSaveSuccess, onSaveError);
            } else {
                Promo.save(vm.promo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:promoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        vm.tableParams = new NgTableParams(initialParams, {
            counts: [],
            paginationMaxBlocks: 10,
            paginationMinBlocks: 2,
            dataset: vm.promo.promoMatieres
        });

        vm.updateEnabled = function(promoMatiere){
            promoMatiere.enabled = !promoMatiere.enabled;
            PromoMatiere.update(promoMatiere);
        }

        vm.update = function(promoMatiere){
            //if(!isNaN(promoMatiere.credit))
            PromoMatiere.update(promoMatiere);
        }

        vm.clear2 = clear2;
        function clear2 () {
            if(vm.teacherModalInstance)
             vm.teacherModalInstance.dismiss();
            if(vm.notesModalInstance)
              vm.notesModalInstance.dismiss();
        }

        vm.openTeacherModal = openTeacherModal;
        function openTeacherModal(promoMatiere){
            vm.promoMatiere = promoMatiere;
            Teacher.getByMatiere(promoMatiere.matiereId)
            .then(function(response){
                 vm.teachers = response.data;
                 vm.teachers.forEach(function(teacher){
                     teacher.selected = existInArray(teacher, promoMatiere.teachers)
                 })
            })
            vm.teacherModalInstance = $uibModal.open({
                templateUrl: 'app/admin/annee/annee-detail-matiere-enseignants-dialog.html',
                scope: $scope,
                backdrop: false,
                size: 'md'
            });
        };

        function existInArray(item, array){
            for (var i=0; i < array.length; ++i) {
                if (array[i].id == item.id) {
                    return true;
                    break;
                }
            }
            return false;
        }

        function indexOf(item, array){
            for (var i=0; i < array.length; ++i) {
                if (array[i].id == item.id) {
                    return i;
                    break;
                }
            }
            return -1;
        }

        function remove(array, element) {
          const index = indexOf(element, array);
          if(index != -1)
          array.splice(index, 1);
        }

        vm.toggleTeacher = function(teacher){
            if(teacher.selected)
                vm.promoMatiere.teachers.push(teacher)
            else
                remove(vm.promoMatiere.teachers, teacher)

            PromoMatiere.update(vm.promoMatiere);
        }

        function savePromoMatiere () {
            vm.isSaving = true;
            PromoMatiere.update(vm.promoMatiere, onSaveSuccessPM, onSaveError);
        }

        function onSaveSuccessPM (response) {
            for (var i=0; i < vm.promo.promoMatieres.length; ++i) {
                if (vm.promo.promoMatieres[i].promoId == response.data.promoId && vm.promo.promoMatieres[i].matiereId == response.data.matiereId) {
                    vm.promo.promoMatieres[i] = response.data;
                    break;
                }
            }
            vm.tableParams.reload();
            vm.teacherModalInstance.dismiss();
            vm.isSaving = false;
        }

        //=======================================
        vm.openNotesModal = openNotesModal;
        function openNotesModal(promoMatiere){
            vm.promoMatiere = promoMatiere;
            NoteInter.getForPromoMatiere(promoMatiere.promoId, promoMatiere.matiereId)
            .then(function(response){
                 vm.noteInters = response.data;
            })
            vm.notesModalInstance = $uibModal.open({
                templateUrl: 'app/admin/annee/annee-detail-matiere-notes-dialog.html',
                scope: $scope,
                backdrop: false,
                size: 'md'
            })
        };

        vm.updateEnabledNoteInter = function(noteInter){
            noteInter.enabledForPromoMatiere = !noteInter.enabledForPromoMatiere;
            if(noteInter.enabledForPromoMatiere){
                var promoMatiereNoteInter = {
                                                noteInterId: noteInter.id,
                                                promoId: vm.promoMatiere.promoId,
                                                matiereId: vm.promoMatiere.matiereId,
                                                weight: noteInter.weight
                                            }
                PromoMatiereNoteInter.save(promoMatiereNoteInter);
            }
            else{
                PromoMatiereNoteInter.delete(noteInter.id, vm.promoMatiere.promoId, vm.promoMatiere.matiereId)
            }
        }

        vm.updateNoteInter = function(noteInter){
            var promoMatiereNoteInter = {
                                            noteInterId: noteInter.id,
                                            promoId: vm.promoMatiere.promoId,
                                            matiereId: vm.promoMatiere.matiereId,
                                            weight: noteInter.weight
                                        }
            PromoMatiereNoteInter.update(promoMatiereNoteInter);
        }

        vm.close = function () {
            if(vm.notesModalInstance)
              vm.notesModalInstance.dismiss();

            Promo.get($stateParams.promoId)
                .then(function(response){
                   vm.tableParams = new NgTableParams(initialParams, {
                       counts: [],
                       paginationMaxBlocks: 10,
                       paginationMinBlocks: 2,
                       dataset: response.data.promoMatieres
                   });
            })
            //$state.go('annee-detail.matieres', null, { reload: 'annee-detail.matieres' });
        }
    }
})();

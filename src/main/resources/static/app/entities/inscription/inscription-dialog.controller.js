(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('InscriptionDialogController', InscriptionDialogController);

    InscriptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
                                                'Inscription', 'Promo', '$filter', 'session', 'Sexe'];

    function InscriptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,
                                                Inscription, Promo, $filter, session, Sexe) {
        var vm = this;
        vm.inscription = entity;
        console.log(vm.inscription);

        Promo.getByAnnee(session.getAnneeId())
            .then(function(response){
                vm.promos = response.data;
        })

        vm.sexes = [];
        Sexe.getAll()
        .then(function(response){
            response.data.forEach(function(sexe) {
               vm.sexes.push({name: sexe.name, value:sexe.value})
            });
        })

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            vm.isSaving = true;
            vm.showErrorAlert = false;
            console.log(vm.inscription)
            if (vm.inscription.id !== null) {
                Inscription.update(vm.inscription, onSaveSuccess, onSaveError);
            } else {
                Inscription.save(vm.inscription, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:inscriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (error) {
            vm.isSaving = false;
            console.log(error)
            vm.errorMessage = error.data.errorMessage;
            vm.showErrorAlert = true;
        }

        $scope.selectedPerson = function(selected){
            if(selected){
                vm.selectedStudent = selected.originalObject;
                vm.inscription.studentId = vm.selectedStudent.id;
            }
        }

        function arrayBufferToBase64(buffer) {
           var binary = '';
           var bytes = new Uint8Array(buffer);
           var len = bytes.byteLength;
           for (var i = 0; i < len; i++) {
             binary += String.fromCharCode(bytes[i]);
           }
           return window.btoa(binary);
        }

    }
})();

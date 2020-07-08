(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ReglementDialogController', ReglementDialogController);

    ReglementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
                                                'Reglement', 'Promo', 'Student', '$filter', 'session'];

    function ReglementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,
                                                Reglement, Promo, Student, $filter, session) {
        var vm = this;
        vm.reglement = entity;
        vm.clear = clear;
        vm.save = save;
        console.log(vm.reglement);

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        Promo.getByAnnee(session.getAnneeId())
        .then(function(response){
            vm.promos = response.data;
            if(vm.reglement.promoId){
                vm.promo =  $filter('filter')(vm.promos, {id: vm.reglement.promoId})[0];
                vm.promoChanged();
            }
        })

        vm.promoChanged =  function(){
            if(vm.reglement.promoId){
                Student.getByPromo(vm.reglement.promoId, "")
                .then(function(response){
                    if(response.data)
                    vm.students = response.data;
                    /*if(vm.reglement.studentId){
                        vm.student =  $filter('filter')(vm.promos, {id: vm.reglement.promoId})[0];
                        vm.promoChanged();
                    }*/
                })
            }
        }

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            vm.isSaving = true;
            if (vm.reglement.id !== null) {
                Reglement.update(vm.reglement, vm.file, onSaveSuccess, onSaveError);
            } else {
                Reglement.save(vm.reglement, vm.file, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            //$scope.$emit('myApp:reglementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        if(vm.reglement.justificatif)
            vm.selectedFile = 'data:image/jpeg;base64,' + vm.reglement.justificatif;

        vm.addJustificatif = function(){
            if(vm.file)
            vm.selectedFile = vm.file;
        };

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

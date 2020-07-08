(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentDialogController', StudentDialogController);

    StudentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
                                                'Student', 'Sexe', 'Promo', '$filter', 'session'];

    function StudentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,
                                                Student, Sexe, Promo, $filter, session) {
        var vm = this;
        vm.student = entity;
        console.log(vm.student);

        vm.studentExist = true;

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
            console.log(vm.student)
            if (vm.student.id !== null) {
                Student.update(vm.student, onSaveSuccess, onSaveError);
            } else {
                Student.save(vm.student, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:studentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
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

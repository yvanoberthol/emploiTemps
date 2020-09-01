(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentCardDialogController', StudentCardDialogController);

    StudentCardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StudentCard', 'Upload'];

    function StudentCardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StudentCard, Upload) {
        var vm = this;
        console.log("studentCard dialog")
        vm.studentCard = entity;
        console.log(vm.studentCard);

        vm.clear = clear;
        vm.save = save;

        vm.images = new Array(2);
        vm.edited = new Array(2);
        vm.labels = ["Recto", "Verso"];

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
        }

        if(vm.studentCard.id){
             StudentCard.getImage(vm.studentCard.id, "recto")
             .then(function(response) {
                 var image = arrayBufferToBase64(response.data);
                 if(image)
                 vm.images[0] = image;
             })
             StudentCard.getImage(vm.studentCard.id, "verso")
             .then(function(response) {
                 var image = arrayBufferToBase64(response.data);
                 if(image)
                 vm.images[1] = image;
             })
        }

        vm.addImage = function(file, index){
            if(file){
                Upload.resize(file, {width: 1200, quality: 0.9}).then(function(image){
                   vm.images[index] = image;
                   vm.edited[index] = true;
                })
            }
        };


        vm.save = save;
        function save () {
            vm.isSaving = true;
            if (vm.studentCard.id !== null) {
                StudentCard.update(vm.studentCard, vm.images[0], vm.images[1], onSaveSuccess, onSaveError);
            } else {
                StudentCard.save(vm.studentCard, vm.images[0], vm.images[1], onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.saveError= false;
            vm.saveSuccess = true;
            vm.clear();
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.saveError= true;
            vm.saveSuccess = false;
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

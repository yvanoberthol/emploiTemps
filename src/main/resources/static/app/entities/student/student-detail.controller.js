(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentDetailController', StudentDetailController);

    StudentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                    'session', 'Promo', 'Student', 'Upload'];

    function StudentDetailController($scope, $rootScope, $stateParams, previousState, entity,
                                    session, Promo, Student, Upload) {
        var vm = this;
        vm.student = entity;
        vm.save = save;
        console.log(vm.student)
        //vm.student.dateNaissance = moment(vm.student.dateNaissance, "DDMMYYYY").format("DD/MM/YYYY")

        vm.previousState = previousState.name;
        vm.selected = 0;
        vm.selectedTab = 0;
        vm.saveError= null;
        vm.saveSuccess = null;

        /*vm.getSolde = function(index){
            if(index > 0){
                return (vm.student.mouvements[index].amount + vm.getSolde(index-1));
            }
            else return vm.student.mouvements[index].amount;
        }*/

        vm.tabSelected = function(index){
            vm.selectedTab = index;
            vm.ligneTrimestre = vm.student.notes[index];
            vm.saveError= null;
            vm.saveSuccess = null;
        }
        vm.tabSelected(0);

        var key = Object.keys(vm.student.notes)[0];
        var matieres = vm.student.notes[key]
        vm.tabSelected(0, matieres);


        function save () {
            vm.isSaving = true;
            if(vm.file){
                Upload.resize(vm.file, {width: 400, quality: 0.8})
                .then(function(image){
                    Student.update(vm.student, image, onSaveSuccess, onSaveError);
                })
            }
            else{
                Student.update(vm.student, null, onSaveSuccess, onSaveError);
            }

        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.saveError= false;
            vm.saveSuccess = true;
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.saveError= true;
            vm.saveSuccess = false;
        }

        if(vm.student.image)
        vm.selectedFile = 'data:image/jpeg;base64,' + vm.student.image;

        vm.addImage = function(){
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

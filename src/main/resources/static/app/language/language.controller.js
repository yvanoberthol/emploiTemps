(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('JhiLanguageController', JhiLanguageController);

    JhiLanguageController.$inject = ['$translate', 'JhiLanguageService', 'tmhDynamicLocale', 'session'];

    function JhiLanguageController ($translate, JhiLanguageService, tmhDynamicLocale, session) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.languages = null;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            session.setLangKey(languageKey);
        }
    }
})();

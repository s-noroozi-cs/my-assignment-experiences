'use strict';

describe('Component: Member List', function () {

  var component, memberResource, ngTableParams, $q, $compile,  $scope;
  var mockMemberResponses = [{
    name: "test-name-1",
    id: 'test-id-1',
    contacts: [{
      name: 'test-contact-name-1',
      email: 'test-contact-email-1',
      phone: 'test-contact-phone-1'
    }]
  },{
    name: "test-name-2",
    id: 'test-id-2',
    contacts: [{
      name: 'test-contact-name-2',
      email: 'test-contact-email-2',
      phone: 'test-contact-phone-2'
    }]
  }];

  beforeEach(function () {
    module('memberApp');
    module('templates');
    inject(function (_$componentController_, _MemberResource_, _NgTableParams_, _$q_, _$compile_,  _$rootScope_) {
      $q = _$q_;
      $scope =  _$rootScope_.$new();
      $compile = _$compile_;
      memberResource = _MemberResource_;
      ngTableParams = _NgTableParams_;
      component = _$componentController_('memberList', {
        MemberResource: memberResource,
        NgTableParams: ngTableParams
      });
    });
  });


  it('should make request to get all members on load', function () {
    var deferred = $q.defer();
    deferred.resolve(mockMemberResponses);
    spyOn(memberResource, "getAll").and.returnValue({$promise : deferred.promise});
    expect(component.tableParams).toBeUndefined();
    component.$onInit();
    expect(memberResource.getAll).toHaveBeenCalled();
  });

  it('should render component', function() {
    var deferred = $q.defer();
    deferred.resolve(mockMemberResponses);
    spyOn(memberResource, "getAll").and.returnValue({$promise : deferred.promise});
    expect(component.tableParams).toBeUndefined();
    component.$onInit();

    var element = $compile(angular.element("<member-list></member-list>"))($scope);
    $scope.$digest();

    expect(element.find("td[data-title-text='Name']")[0].innerHTML).toBe('test-name-1');
    expect(element.find("td[data-title-text='Identifier']")[0].innerHTML).toBe('test-id-1');
    expect(element.find("td[data-title-text='Name']")[1].innerHTML).toBe('test-name-2');
    expect(element.find("td[data-title-text='Identifier']")[1].innerHTML).toBe('test-id-2');
  });
});
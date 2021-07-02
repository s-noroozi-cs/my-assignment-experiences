'use strict';

describe('Component: MemberAdd', function () {

  var component, memberResource, $q, $compile, $scope;
  var mockMemberResponse = {
    name: "test-name",
    contacts: [{
      name: 'test-contact-name',
      email: 'test-contact-email',
      phone: 'test-contact-phone'
    }]
  };

  beforeEach(function () {
    module('memberApp');
    module('templates');
    inject(function (_$componentController_, _MemberResource_, _$q_, _$compile_, _$rootScope_) {
      $q = _$q_;
      $scope = _$rootScope_.$new();
      $compile = _$compile_;
      memberResource = _MemberResource_;
      component = _$componentController_('memberAdd', {
        MemberResource: memberResource
      });
    });
  });

  it('should successfully make request to add member', function () {
    spyOn(memberResource, "save").and.callFake(function(params, successCallBack) {
      successCallBack({
        name: 'test-name',
        id: 'test-id'
      });
    });

    component.member={};
    component.member.name = mockMemberResponse.name;
    component.member.contacts=[{}];
    component.member.contacts[0].name = mockMemberResponse.contacts[0].name;
    component.member.contacts[0].email = mockMemberResponse.contacts[0].email;
    component.member.contacts[0].phone = mockMemberResponse.contacts[0].phone;

    expect(component.successMsg).toBeUndefined();
    expect(component.successId).toBeUndefined();

    component.addMember(); // method under test

    expect(memberResource.save).toHaveBeenCalledWith({
      name:component.member.name,
      contacts: [{
        name:component.member.contacts[0].name,
        email:component.member.contacts[0].email,
        phone:component.member.contacts[0].phone
      }]
    }, jasmine.any(Function), jasmine.any(Function));

    expect(component.successMsg.indexOf('test-name')).not.toBe(-1);
    expect(component.successId).toBe("test-id");

  });

  it('should handle failed request to add member', function () {
    spyOn(memberResource, "save").and.callFake(function(params, successCallBack, failureCallBack) {
      failureCallBack({
        statusText: '404',
        data: 'not found'
      });
    });
    component.member={};
    component.member.name = mockMemberResponse.name;
    component.member.contacts=[{}];
    component.member.contacts[0].name = mockMemberResponse.contacts[0].name;
    component.member.contacts[0].email = mockMemberResponse.contacts[0].email;
    component.member.contacts[0].phone = mockMemberResponse.contacts[0].phone;

    expect(component.errorMsg).toBeUndefined();

    component.addMember(); // method under test

    expect(memberResource.save).toHaveBeenCalledWith({
      name:component.member.name,
      contacts: [{
        name:component.member.contacts[0].name,
        email:component.member.contacts[0].email,
        phone:component.member.contacts[0].phone
      }]
    }, jasmine.any(Function), jasmine.any(Function));

    expect(component.errorMsg.indexOf('404')).not.toBe(-1);
    expect(component.errorMsg.indexOf('not found')).not.toBe(-1);
  });

  // TODO implement the test that checks that the <member-add></member-add> component when used renders the add member
  // form
  it('should render component', function() {

    component.member.name="test-name-1";
    component.member.contacts=[];
    expect(component.member.contacts.length).toBe(0);

    component.addContact();
    expect(component.member.contacts.length).toBe(1);

    var element = $compile(angular.element("<member-add></member-add>"))($scope);
    $scope.$digest();

    // console.log(element);
    //log rendered template to console but, unfortunately did not render model data on template
  });
});
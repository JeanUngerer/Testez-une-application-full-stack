describe('admin info spec', () => {

  it('should have valid user info', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="me"]').click();
    cy.url().should('include', '/me')
    cy.get('[data-cy="name"]').should('have.text', 'Name: Admin ADMIN');
    cy.get('[data-cy="mail"]').should('have.text', 'Email: yoga@studio.com');
  });
});

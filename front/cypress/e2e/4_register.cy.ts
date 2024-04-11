    describe('Register spec', () => {
        it('should have submit button disabled while form is not filled', () => {
            cy.visit('http://localhost:4200/register')
            cy.get('[type="submit"]').should('be.disabled');
        })

        it('should redirect the user to a success page when filling the form and clicking submit, and then navigate to login page', () => {
            cy.visit('http://localhost:4200/register')
            cy.get('input[formControlName="firstName"]').type('TestFirstName')
            cy.get('input[formControlName="lastName"]').type('TestLastName')
            cy.get('input[formControlName="email"]').type('testemail@test.com')
            cy.get('input[formControlName="password"]').type('testPassword')
            cy.get('[type="submit"]').should('be.enabled').click();

            // Added check for navigation to login Page
            cy.url().should('eq', 'http://localhost:4200/login')
        })

        it('should try to register an already used email and show error', () => {
          cy.visit('http://localhost:4200/register')
          cy.get('input[formControlName="firstName"]').type('TestFirstName')
          cy.get('input[formControlName="lastName"]').type('TestLastName')
          cy.get('input[formControlName="email"]').type('testemail@test.com')
          cy.get('input[formControlName="password"]').type('testPassword')
          cy.get('[type="submit"]').should('be.enabled').click();

          cy.get('[data-cy="error"]').should('exist')
        });
    })

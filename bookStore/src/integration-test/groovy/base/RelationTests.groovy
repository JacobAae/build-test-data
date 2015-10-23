package base

import bookstore.Author
import bookstore.Book
import bookstore.Invoice
import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import human.Arm
import human.Face
import magazine.Issue
import magazine.Page
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

@TestMixin(IntegrationTestMixin)
class RelationTests extends GroovyTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testOneToOneCascades() {
        def domainObject = Face.build()
        assertNotNull domainObject
        assertNotNull domainObject.id
        assertNotNull domainObject.nose
        assertNotNull domainObject.nose.id
    }

    void testBelongsToGetsHasMany() {
        def bookDomain = new DefaultGrailsDomainClass(Book)
        def domainProp = bookDomain.properties.find {it.name == 'author' }
        assertTrue domainProp.isManyToOne()
        def domainObject = Book.build()
        assertNotNull domainObject
        assertNotNull domainObject.author
        assertNotNull domainObject.author.books
        assertNotNull domainObject.author.books.find {book ->
            book == domainObject
        }
        assertNotNull domainObject.id
    }

    void testHasManyNullableFalse() {
        def authorDomain = new DefaultGrailsDomainClass(Author)
        def domainProp = authorDomain.properties.find {it.name == 'books' }
        assertTrue domainProp.isOneToMany()
        assertEquals false, domainProp.isOptional()
        def bookConstrainedProperty = authorDomain.constrainedProperties['books']
        assertEquals false, bookConstrainedProperty.isNullable()
        def domainObject = Author.build()
        assertNotNull domainObject
        assertNotNull domainObject.id
        assertNotNull domainObject.books
        assertNotNull domainObject.address
        assertEquals domainObject.books.size(), 2
        domainObject.books.each {book ->
            assertEquals book.author, domainObject
        }
    }

    void testManyToManyNullableFalse() {
        def invoiceDomain = new DefaultGrailsDomainClass(Invoice)
        def domainProp = invoiceDomain.properties.find {it.name == 'books' }
        assertTrue domainProp.isManyToMany()
        assertEquals false, domainProp.isOptional()
        def bookConstrainedProperty = invoiceDomain.constrainedProperties['books']
        assertEquals false, bookConstrainedProperty.isNullable()
        def domainObject = Invoice.build()
        assertNotNull domainObject
        assertNotNull domainObject.id
        assertNotNull domainObject.books
        assertEquals domainObject.books.size(), 3
    }

    void testParentCollectionUpdatedWhenChildAutomaticallyAdded() {
        def page = Page.build()
        assertNotNull page
        assertNotNull page.issue
        assertEquals([page.id], page.issue.pages.id)
    }

    void testParentCollectionUpdatedWhenChildManuallyAdded() {
        def issue = new Issue(title: "title").save(failOnError: true)
        assertNotNull issue

        def page = Page.build(issue: issue)

        assertNotNull page
        assertEquals issue, page.issue

        assertEquals([page.id], issue.pages.id)
    }

	void testHasOne() {
		def arm = Arm.build()
		def hand = arm.hand
		assertNotNull arm
		assertNotNull hand
		assertEquals arm, hand.arm
		assertEquals hand, arm.hand
	}

}

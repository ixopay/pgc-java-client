
package pgc.client.model.transaction;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for voidType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="voidType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://paymentgateway.cloud/Schema/V2/Transaction}baseTransactionWithReferenceType"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "voidType")
public class VoidType
    extends BaseTransactionWithReferenceType
{


    /**
     * Default no-arg constructor
     * 
     */
    public VoidType() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public VoidType(final String transactionToken, final String transactionId, final String additionalId1, final String additionalId2, final CustomerType customer, final CreditCardCustomerType creditCardCustomer, final IbanCustomerType ibanCustomer, final List<ExtraDataType> extraData, final String merchantMetaData, final RequestType request, final String referenceTransactionId, final String referenceCustomerId, final String referenceId2, final String referenceId3, final String referenceId4) {
        super(transactionToken, transactionId, additionalId1, additionalId2, customer, creditCardCustomer, ibanCustomer, extraData, merchantMetaData, request, referenceTransactionId, referenceCustomerId, referenceId2, referenceId3, referenceId4);
    }

}

package org.academiadecodigo.javabank.view;

import org.academiadecodigo.javabank.controller.RecipientsController;
import org.academiadecodigo.javabank.model.Recipient;

import java.util.List;

/**
 * A view used to display a list of recipients
 *
 * @see RecipientsController
 */
public class RecipientsView implements View {

    private RecipientsController recipientsController;

    /**
     * Sets the controller responsible for rendering the view
     *
     * @param recipientsController the controller to set
     */
    public void setRecipientsController(RecipientsController recipientsController) {
        this.recipientsController = recipientsController;
    }

    /**
     * @see View#show()
     */
    @Override
    public void show() {
        showRecipients();
    }

    private void showRecipients() {

        System.out.println("\n" + Messages.VIEW_RECIPIENTS_MESSAGE + "\n");

        List<Recipient> recipients = recipientsController.getRecipients();
        for (Recipient recipient : recipients) {

            StringBuilder sb = new StringBuilder();

            sb.append(recipient.getAccountNumber());
            sb.append("\t");
            sb.append(recipient.getName());
            sb.append("\t");
            sb.append(recipient.getEmail());
            sb.append("\t");
            sb.append(recipient.getPhone());
            sb.append("\t");
            sb.append(recipient.getDescription());

            System.out.println(sb.toString());
        }
    }
}

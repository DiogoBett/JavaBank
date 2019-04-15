package org.academiadecodigo.javabank.view;

import org.academiadecodigo.javabank.controller.RecipientsController;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class RecipientsViewTest {

    private RecipientsView view;
    private RecipientsController controller;
    private PrintStream out;
    private List<Recipient> recipients;

    @Before
    public void setup() {

        view = new RecipientsView();
        controller = mock(RecipientsController.class);
        out = mock(PrintStream.class);
        recipients = new LinkedList<>();

        view.setRecipientsController(controller);
        System.setOut(out);

        when(controller.getRecipients()).thenReturn(recipients);
    }

    @Test
    public void showRecipients() {

        recipients.add(mock(Recipient.class));
        recipients.add(mock(Recipient.class));
        recipients.add(mock(Recipient.class));

        view.show();

        verify(controller).getRecipients();
        for (Recipient recipient : recipients) {
            verify(recipient).getName();
            verify(recipient).getEmail();
            verify(recipient).getDescription();
        }
    }
}

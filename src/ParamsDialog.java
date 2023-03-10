import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParamsDialog extends JDialog{

    public final int DEFAULT_COL_COUNT = 5;
    public final int DEFAULT_ROW_COUNT = 5;
    private JPanel mainDialog;
    private JSpinner rowSpinner;
    private JSpinner colSpinner;
    private JButton buttonOK;
    private JButton buttonNewGame;
    private JButton buttonCancel;
    private JSlider sliderCellSize;

    private GameParams params;
    private JTable gameFieldJTable;
    private ActionListener newGameAction;

    private int oldCellSize;

    public ParamsDialog(GameParams params, JTable gameFieldJTable, ActionListener newGameAction) {
        this.setTitle("Settings");
        this.setContentPane(mainDialog);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.pack();

        this.setResizable(false);

        this.params = params;
        this.gameFieldJTable = gameFieldJTable;
        this.newGameAction = newGameAction;

        this.oldCellSize = gameFieldJTable.getRowHeight();
        sliderCellSize.addChangeListener(e -> {
            int value = sliderCellSize.getValue();
            JTableUtils.resizeJTableCells(gameFieldJTable, value, value);
        });
        buttonCancel.addActionListener(e -> {
            JTableUtils.resizeJTableCells(gameFieldJTable, oldCellSize, oldCellSize);
            this.setVisible(false);
        });
        buttonNewGame.addActionListener(e -> {
            buttonOK.doClick();
            if (newGameAction != null) {
                newGameAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "newGame"));
            }
        });
        buttonOK.addActionListener(e -> {
            if ((int) rowSpinner.getValue() <= 10 && (int) colSpinner.getValue() <= 10) {
                params.setRowCount((int) rowSpinner.getValue());
                params.setColumnCount((int) colSpinner.getValue());
            }
            else{
                JOptionPane.showMessageDialog(new JFrame(), "A lot of Cells!!!", "Message", JOptionPane.INFORMATION_MESSAGE);
                params.setRowCount(DEFAULT_ROW_COUNT);
                params.setColumnCount(DEFAULT_COL_COUNT);
            }

            oldCellSize = gameFieldJTable.getRowHeight();
            this.setVisible(false);
        });
    }

    public void updateView() {
        rowSpinner.setValue(params.getRowCount());
        colSpinner.setValue(params.getColumnCount());
        sliderCellSize.setValue(gameFieldJTable.getRowHeight());
    }
}

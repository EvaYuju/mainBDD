package com.main.controller;

import com.main.model.BdManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.main.model.Cuenta;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
/**
 * Clase principal que controla los elementos del visor
 */
public class ControllerVisor implements Initializable {

    // public MenuItem menuItem2;
    @FXML
    private Button bDerecha;

    @FXML
    private Button bIzquierda;

    @FXML
    private Button bAceptar;

    @FXML
    private Button bCancelar;
    @FXML
    private TextField tfFecha;

    @FXML
    private TextField tfNum;

    @FXML
    private TextField tfSaldo;

    @FXML
    private TextField tfTitular;
    @FXML
    private Label lTitulo;

    //Lista de cuentas de la aplicación
    ArrayList<Cuenta> listaCuentas = new ArrayList<>();
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private static final String NUEVA = "Nueva";
    private static final String WRONG_STYLE = "-fx-background-color: red";
    /**
     * Función ejecutada al iniciar la aplicación
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BdManager bdManager = new BdManager();
        try {
            Statement statament = bdManager.abrirBD();
            String consulta = "SELECT * FROM numCuenta";
            ResultSet resultSet = statament.executeQuery(consulta);
            while(resultSet.next()) {
                int numero = resultSet.getInt("numero");
                String titular = resultSet.getString("titular");
                String nacionalidad = resultSet.getString("nacionalidad");
                String fecha = resultSet.getString("fecha");
                Double saldo = resultSet.getDouble("saldo");

                listaCuentas.add(new Cuenta(numero, titular, nacionalidad, fecha, saldo));
            }
        } catch (Exception e) {
            System.out.println("No hay datos");
        }

    
        tfNum.setText(listaCuentas.get(0).getNumCuenta());
        tfTitular.setText(listaCuentas.get(0).getTitular());
        tfFecha.setText(formato.format(listaCuentas.get(0).getFechaApertura()));
        tfSaldo.setText(String.valueOf(listaCuentas.get(0).getSaldo()));

    }
    /**
     * Función que controla el botón para ir a la derecha
     * @param event
     */
    @FXML
    void pressDerecha(ActionEvent event) {

        Cuenta cuentaActual = getCuentaActual(tfNum.getText());
        if (cuentaActual != null) {
            int pos = listaCuentas.indexOf(cuentaActual);

            if (pos < listaCuentas.size() - 1) {
                if (pos >= 0) {
                    bIzquierda.setVisible(true);
                }
                if (pos == listaCuentas.size() - 2) {
                    bDerecha.setText(NUEVA);
                }


                tfNum.setText(listaCuentas.get(pos + 1).getNumCuenta());
                tfTitular.setText(listaCuentas.get(pos + 1).getTitular());
                tfFecha.setText(formato.format(listaCuentas.get(pos + 1).getFechaApertura()));
                tfSaldo.setText(String.valueOf(listaCuentas.get(pos + 1).getSaldo()));

            }
            if (pos == listaCuentas.size() - 1) {
                lTitulo.setText("VISOR DE LAS CUENTAS NUEVAS");
                tfNum.setText("");
                tfTitular.setText("");
                tfFecha.setText("");
                tfSaldo.setText("");
                bDerecha.setVisible(false);
                bIzquierda.setVisible(false);
                bAceptar.setVisible(true);
                bAceptar.setDisable(false);
                bCancelar.setVisible(true);
                bCancelar.setDisable(false);
            }
        }
    }
    /**
     * Función que controla el botón de ir a la izquierda
     * @param event
     */
    @FXML
    void pressIzquierda(ActionEvent event) {
        Cuenta cuentaActual = getCuentaActual(tfNum.getText());

        if (cuentaActual != null) {
            int pos = listaCuentas.indexOf(cuentaActual);
            if (pos > 0) {
                if (pos == 1) {
                    bIzquierda.setVisible(false);
                }
                if (pos < listaCuentas.size()) {
                    bDerecha.setText(">>");

                }
                tfNum.setText(listaCuentas.get(pos - 1).getNumCuenta());
                tfTitular.setText(listaCuentas.get(pos - 1).getTitular());
                tfFecha.setText(formato.format(listaCuentas.get(pos - 1).getFechaApertura()));
                tfSaldo.setText(String.valueOf(listaCuentas.get(pos - 1).getSaldo()));
            }
        }

    }
    /**
     * Función que controla el botón de Aceptar, que aparece al crear una cuenta nueva
     * Se controlará que cada uno de los campos se haya escrito correctamente
     * @param event
     */
    @FXML
    void pressAceptar(ActionEvent event) {
        boolean cuentaExiste = false;
        boolean campoVacio = false;

        int numCuentaNueva = 0;

        for(Cuenta c : listaCuentas){
            if(c.getNumCuenta().equals(tfNum.getText())){
                tfNum.setStyle(WRONG_STYLE);
                campoVacio = true;
            }
        }

        if(tfTitular.getText().equals("")){
            tfTitular.setStyle(WRONG_STYLE);
            campoVacio = true;
        }else {
            tfTitular.setStyle(null);
        }


        if(tfFecha.getText().equals("")){
            tfFecha.setStyle(WRONG_STYLE);
            campoVacio = true;
        }else {
            tfFecha.setStyle(null);
        }


        if(tfSaldo.getText().equals("")){
            tfSaldo.setStyle(WRONG_STYLE);
            campoVacio = true;
        }else {
            tfSaldo.setStyle(null);
        }

        try{
            numCuentaNueva = Integer.parseInt(tfNum.getText());
            tfNum.setStyle(null);
        } catch (Exception e){
            tfNum.setStyle(WRONG_STYLE);
            campoVacio = true;
        }
        Date fechaCuentaNueva = null;

        try{
            fechaCuentaNueva = formato.parse(tfFecha.getText());
            tfFecha.setStyle(null);
        } catch (ParseException e) {
            tfFecha.setStyle(WRONG_STYLE);
            campoVacio = true;
        }

        double saldoCuentaNueva = 0;
        try{
            saldoCuentaNueva = Double.parseDouble(tfSaldo.getText());
            tfSaldo.setStyle(null);
        } catch (Exception e){
            tfSaldo.setStyle(WRONG_STYLE);
            campoVacio = true;
        }
        // Si todos los campos están escritos correctamente y no existe una cuenta con ese número se añadirá a la lista
        if(!cuentaExiste && !campoVacio) {

            int numero = 0;
            listaCuentas.add(new Cuenta(numero, String.valueOf(numCuentaNueva), tfTitular.getText()
                        , formato.format(fechaCuentaNueva), saldoCuentaNueva));
            backFromInsert();

        }
    }
    /**
     * Función que devuelve todos los elementos al estado anterior a la creación de cuenta nueva
     */
    private void backFromInsert() {
        lTitulo.setText("VISOR DE LAS CUENTAS EXISTENTES");
        tfNum.setStyle(null);
        tfTitular.setStyle(null);
        tfSaldo.setStyle(null);
        tfFecha.setStyle(null);
        bDerecha.setVisible(true);
        bIzquierda.setVisible(true);
        bAceptar.setVisible(false);
        bAceptar.setDisable(true);
        bCancelar.setVisible(false);
        bCancelar.setDisable(true);
        bIzquierda.setText("<<");
        bDerecha.setText(NUEVA);
        tfNum.setText(listaCuentas.get(listaCuentas.size() - 1).getNumCuenta());
        tfTitular.setText(listaCuentas.get(listaCuentas.size() - 1).getTitular());
        tfFecha.setText(formato.format(listaCuentas.get(listaCuentas.size() - 1).getFechaApertura()));
        tfSaldo.setText(String.valueOf(listaCuentas.get(listaCuentas.size() - 1).getSaldo()));
    }
    /**
     * Función que controla cuando se pulsa el botón de cancelar en la pantalla de creación de cuenta
     * @param event
     */
    @FXML
    void pressCancelar(ActionEvent event) {
        backFromInsert();

    }
    /**
     * Función que devuelve la cuenta cuyo número coincida con el que se pasa por parámetro
     * @param num número con el que compararemos los números de todas las cuentas
     * @return
     */
    public Cuenta getCuentaActual(String num) {
        for (Cuenta c : listaCuentas) {
            if (num.equals(c.getNumCuenta())) {
                return c;
            }
        }
        return null;
    }
    /**
     * Función que devuelve el número de cuentas actuales en la lista
     * @return el número de cuentas en la lista
     */
    public int getNumCuentas(){
        return listaCuentas.size();
    }

    /**
     * Función que devuelve el número de cuentas con saldo mayor a 50000
     * @return el número de cuentas en la lista
     */
    public int getCuentasSaldoAlto(){
        int i=0;
        for(Cuenta c: listaCuentas){
            if(c.getSaldo() >= 50000){
                i++;
            }
        }
        return i;
    }
}

package edu.masanz.da.ta.dao;

import edu.masanz.da.ta.dto.*;
import edu.masanz.da.ta.utils.Security;

import java.util.*;

import static edu.masanz.da.ta.conf.Ctes.*;
import static edu.masanz.da.ta.conf.Ini.*;

/**
 * Clase que simula la capa de acceso a datos. Cuando veamos las interfaces crearemos una interfaz para esta clase.
 * También crearemos una clase que implemente esa interfaz y que se conecte a una base de datos relacional.
 * Y una clase servicio que podrá utilizar cualquiera de las dos implementaciones, la simulada, la real u otra.
 * Por ahora, simplemente es una clase con métodos estáticos que simulan la interacción con una base de datos.
 */
public class Dao {


    //region Colecciones que simulan la base de datos
    private static Map<String, Usuario> mapaUsuarios;

    private static Map<Long, Item> mapaItems;

    private static Map<Long, List<Puja>> mapaPujas;
    //endregion

    //region Inicialización de la base de datos simulada
    public static void ini() {
        iniMapaUsuarios();
        iniMapaItems();
        iniMapaPujas();
    }

    private static void iniMapaUsuarios() {
        // TODO 01 iniMapaUsuarios
        mapaUsuarios = new HashMap<>();
        for (String user : USUARIOS){
            String[] arrayUsuario= user.split(SPLITTER);
            mapaUsuarios.put(arrayUsuario[0], new Usuario(arrayUsuario[0],arrayUsuario[1],arrayUsuario[2],arrayUsuario[3]));
        }
    }

    private static void iniMapaItems() {
        // TODO 02 iniMapaItems
        mapaItems = new HashMap<>();
        for (String item : ITEMS){
            String[] arrayItem= item.split(SPLITTER);
            mapaItems.put(Long.parseLong(arrayItem[0]), new Item(Long.parseLong(arrayItem[0]), arrayItem[1], arrayItem[2], Integer.parseInt(arrayItem[3]), arrayItem[4], arrayItem[5], Integer.parseInt(arrayItem[6]), Boolean.parseBoolean(arrayItem[7])));
        }
    }

    private static void iniMapaPujas() {
        // TODO 03 iniMapaPujas
        mapaPujas = new HashMap<>();
        Set<Long> idPujas= new HashSet<>();
        for (String pujas : PUJAS) {
            String[] arrayPujas = pujas.split(SPLITTER);
            idPujas.add(Long.parseLong(arrayPujas[0]));

        }
        for (Long idPuja : idPujas) {
            List<Puja> listaPuja = new ArrayList<>();
            for (String puja : PUJAS) {
                String[] arrayPujas = puja.split(SPLITTER);
                if (Long.parseLong(arrayPujas[0])   == idPuja){
                    listaPuja.add(new Puja(Long.parseLong(arrayPujas[0]),arrayPujas[1],Integer.parseInt(arrayPujas[2]),arrayPujas[3]));
                }
            }
            mapaPujas.put(idPuja,listaPuja);
        }
    }
    //endregion

    //region Usuarios
    public static boolean autenticar(String nombreUsuario, String password) {
//        return password.equals("1234");
        // TODO 04 autenticar
        if(Security.validateHashedPasswordSalt(password,mapaUsuarios.get(nombreUsuario).getSal(), mapaUsuarios.get(nombreUsuario).getHashPwSal())){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean esAdmin(String nombreUsuario) {
//        return nombreUsuario.equalsIgnoreCase("Admin");
        // TODO 05 esAdmin
        if(mapaUsuarios.get(nombreUsuario).getRol().equals("Admin")){
            return true;
        } else{
        return false;
        }
    }

    public static List<Usuario> obtenerUsuarios() {
        // TODO 06 obtenerUsuarios
        return null;
    }

    public static boolean crearUsuario(String nombre, String password, boolean esAdmin) {
        // TODO 07 crearUsuario
        String contrasenaSalt= Security.generateSalt();
        String contrasenaHas = Security.hash(password);
        if(esAdmin){
            mapaUsuarios.put(nombre, new Usuario(nombre,contrasenaSalt,contrasenaHas,ROL_ADMIN));
        } else{
            mapaUsuarios.put(nombre, new Usuario(nombre,contrasenaSalt,contrasenaHas,ROL_USER));
        }

        return true;
    }

    public static boolean modificarPasswordUsuario(String nombre, String password) {
        // TODO 08 modificarPasswordUsuario
        return false;
    }

    public static boolean modificarRolUsuario(String nombre, String rol) {
        // TODO 09 modificarRolUsuario
        if (mapaUsuarios.containsKey(nombre)){
            mapaUsuarios.get(nombre).setRol(rol);
            return true;
        }
        return false;
    }

    public static boolean eliminarUsuario(String nombre) {
        // TODO 10 eliminarUsuario
        if (mapaUsuarios.containsKey(nombre)){
            mapaUsuarios.remove(nombre);
            return true;
        }else {
            return false;
        }
    }

    //endregion

    //region Validación de artículos
    public static List<Item> obtenerArticulosPendientes() {
        // TODO 11 obtenerArticulosPendientes
        return null;
    }

    public static boolean validarArticulo(long id, boolean valido) {
        // TODO 12 validarArticulo
        if (valido){
            mapaItems.get(id).setEstado(EST_ACEPTADO);
            return true;
        } else {
            mapaItems.get(id).setEstado(EST_RECHAZADO);
            return true;
        }
    }

    public static boolean validarTodos() {
        // TODO 13 validarTodos
        for (Long items : mapaItems.keySet()) {
            validarArticulo(items,true);
        }
        return true;
    }
    //endregion

    //region Gestión de artículos y pujas de administrador
    public static List<ItemPujas> obtenerArticulosConPujas() {
        // TODO 14 obtenerArticulosConPujas
        return null;
    }

    public static boolean resetearSubasta() {
        // TODO 15 resetearSubasta
        for (Long l : mapaItems.keySet()) {
            if (!mapaItems.get(l).isHistorico()){
                mapaItems.get(l).setHistorico(true);
            }
        }
        return true;
    }

    public static List<PujaItem> obtenerHistoricoGanadores() {
        // TODO 16 obtenerHistoricoGanadores
        return null;
    }
    //endregion

    //region Acciones por parte de usuario normal (no admin)

    public static Item obtenerArticuloPujable(long idArt) {
        // TODO 17 obtenerArticuloPujable
        if( mapaItems.get(idArt).getEstado()==EST_ACEPTADO){
            return mapaItems.get(idArt);
        }else {
            return null;
        }
    }

    public static List<Item> obtenerArticulosPujables() {
        // TODO 18 obtenerArticulosPujables
        return null;
    }

    public static boolean pujarArticulo(long idArt, String nombre, int precio) {
        // TODO 19 pujarArticulo
        return false;
    }

    public static List<PujaItem> obtenerPujasVigentesUsuario(String nombreUsuario) {
        // TODO 20 obtenerPujasVigentesUsuario
        return null;
    }

    public static boolean ofrecerArticulo(Item item) {
        // TODO 21 ofrecerArticulo
        if (item==null || mapaItems.containsKey(item.getId())){
            return false;
        }else {
            item.setEstado(EST_PENDIENTE);
            mapaItems.put(item.getId(), item);
            return true;
        }
    }
    }

    //endregion


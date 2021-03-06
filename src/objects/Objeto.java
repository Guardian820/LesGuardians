package objects;

import common.Constantes;
import common.F�rmulas;
import common.LesGuardians;
import common.World;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Acao;
import objects.Almas;
import objects.Incarnacao;
import objects.Personagens;
import objects.Pets;
import objects.Set_Vivo;
import objects.SpellEffect;

public class Objeto {
    protected ObjetoModelo _modelo;
    protected int _cantidad = 1;
    protected int _posicion = -1;
    protected int _id;
    private Personagens.Stats _stats = new Personagens.Stats();
    private ArrayList<SpellEffect> _efectos = new ArrayList();
    private Map<Integer, String> _textoStats = new TreeMap<Integer, String>();
    private ArrayList<String> _hechizoStats = new ArrayList();
    protected int _objevivoID;
    protected int _idObjModelo;

    public Objeto() {
        this._cantidad = 1;
        this._posicion = -1;
        this._stats = new Personagens.Stats();
        this._efectos = new ArrayList();
        this._textoStats = new TreeMap<Integer, String>();
        this._hechizoStats = new ArrayList();
    }

    public Objeto(int id, int modeloBD, int cant, int pos, String strStats, int idObjevi) {
        this._id = id;
        this._modelo = World.getObjModelo(modeloBD);
        this._cantidad = cant;
        this._posicion = pos;
        this._stats = new Personagens.Stats();
        this._textoStats = new TreeMap<Integer, String>();
        this._hechizoStats = new ArrayList();
        this._objevivoID = idObjevi;
        this._idObjModelo = modeloBD;
        this.convertirStringAStats(strStats);
    }

    public Objeto(int id, int modeloBD, int cant, int pos, String strStats, ArrayList<SpellEffect> efectos, int idObjevi) {
        this._id = id;
        this._modelo = World.getObjModelo(modeloBD);
        this._cantidad = cant;
        this._posicion = pos;
        this._stats = new Personagens.Stats();
        this._textoStats = new TreeMap<Integer, String>();
        this._hechizoStats = new ArrayList();
        this._efectos = efectos;
        this._objevivoID = idObjevi;
        this._idObjModelo = modeloBD;
        this.convertirStringAStats(strStats);
    }

    public int getObjeviID() {
        return this._objevivoID;
    }

    public void setObjeviID(int id) {
        this._objevivoID = id;
    }

    public int getIDModelo() {
        return this._idObjModelo;
    }

    public void setIDModelo(int idModelo) {
        this._idObjModelo = idModelo;
        this._modelo = World.getObjModelo(idModelo);
    }

    public void convertirStringAStats(String strStats) {
        String[] split;
        this._stats = new Personagens.Stats();
        this._textoStats = new TreeMap<Integer, String>();
        this._hechizoStats = new ArrayList();
        this._efectos = new ArrayList();
        String[] arrstring = split = strStats.split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            try {
                String hechizo = s;
                String[] stats = s.split("#");
                int statID = Integer.parseInt(stats[0], 16);
                if (statID == 998 || statID == 997 || statID == 996 || statID == 994 || statID == 989 || statID == 988 || statID == 987 || statID == 986 || statID == 985 || statID == 983) {
                    this._textoStats.put(statID, stats[4]);
                    continue;
                }
                if (statID == 800 || statID == 811 || statID == 961 || statID == 962 || statID == 960 || statID == 950 || statID == 951) {
                    this._textoStats.put(statID, stats[3]);
                    continue;
                }
                if (statID >= 281 && statID <= 294) {
                    this._hechizoStats.add(hechizo);
                    continue;
                }
                boolean siguiente = true;
                int[] arrn = Constantes.ID_EFECTOS_ARMAS;
                int n2 = Constantes.ID_EFECTOS_ARMAS.length;
                for (int j = 0; j < n2; ++j) {
                    int a = arrn[j];
                    if (a != statID) continue;
                    String args = String.valueOf(stats[1]) + ";" + stats[2] + ";-1;-1;0;" + stats[4];
                    this._efectos.add(new SpellEffect(statID, args, 0, -1));
                    siguiente = false;
                }
                if (!siguiente) continue;
                int valor = Integer.parseInt(stats[1], 16);
                this._stats.addStat(statID, valor);
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (LesGuardians.ARMAS_ENCARNACAO.contains(this._idObjModelo)) {
            Incarnacao encarnacion = World.getEncarnacion(this._id);
            if (encarnacion == null) {
                this._stats.addStat(118, 1);
                this._stats.addStat(119, 1);
                this._stats.addStat(123, 1);
                this._stats.addStat(126, 1);
            } else {
                int valor = encarnacion.getNivel() * 5;
                this._stats.addStat(118, valor);
                this._stats.addStat(119, valor);
                this._stats.addStat(123, valor);
                this._stats.addStat(126, valor);
            }
        }
    }

    public String convertirStatsAString() {
        int statID;
        int tipoModelo = this._modelo.getTipo();
        int idSetModelo = this._modelo.getSetID();
        if (tipoModelo == 83) {
            return this._modelo.getStringStatsObj();
        }
        if (LesGuardians.ARMAS_ENCARNACAO.contains(this._idObjModelo)) {
            return this._modelo._statsModelo;
        }
        if ((tipoModelo == 18 || tipoModelo == 90) && World.getIdTodasMascotas().contains(this._id)) {
            Pets mascota = World.getMascota(this._id);
            if (tipoModelo == 18) {
                return mascota.analizarStatsMascota();
            }
            return mascota.analizarStatsFantasma();
        }
        if (tipoModelo == 113) {
            if (this._objevivoID == 0) {
                int tipo = 0;
                if (this._idObjModelo == 9233) {
                    tipo = 17;
                } else if (this._idObjModelo == 9234) {
                    tipo = 16;
                } else if (this._idObjModelo == 9255) {
                    tipo = 1;
                } else if (this._idObjModelo == 9256) {
                    tipo = 9;
                }
                return "3cc#0#0#" + Integer.toHexString(1) + "," + "3cb#0#0#1," + "3cd#0#0#" + Integer.toHexString(tipo) + "," + "3ca#0#0#0," + "3ce#0#0#0";
            }
            if (this._objevivoID != 0) {
                Set_Vivo objevi = World.getObjevivos(this._objevivoID);
                if (objevi != null) {
                    return objevi.convertirAString();
                }
                this._objevivoID = 0;
            }
        }
        if (this._stats.getStatsComoMap().isEmpty() && this._hechizoStats.isEmpty() && this._efectos.isEmpty() && this._textoStats.isEmpty()) {
            return "";
        }
        String stats = "";
        boolean primero = false;
        if (idSetModelo >= 81 && idSetModelo <= 92 || idSetModelo >= 201 && idSetModelo <= 212) {
            for (String string : this._hechizoStats) {
                if (primero) {
                    stats = String.valueOf(stats) + ",";
                }
                String[] hechi = string.split("#");
                int idhechizo = Integer.parseInt(hechi[1], 16);
                String cantidad = "";
                try {
                    cantidad = hechi[3].split("\\+")[1];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    cantidad = hechi[3].split("\\+")[0];
                }
                stats = String.valueOf(stats) + hechi[0] + "#" + hechi[1] + "#0#" + cantidad + "#0d0+" + idhechizo;
                primero = true;
            }
        }
        for (SpellEffect spellEffect : this._efectos) {
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            String[] infos = spellEffect.getArgs().split(";");
            try {
                stats = String.valueOf(stats) + Integer.toHexString(spellEffect.getEfectoID()) + "#" + infos[0] + "#" + infos[1] + "#0#" + infos[5];
            }
            catch (Exception e) {
                continue;
            }
            primero = true;
        }
        for (Map.Entry entry : this._stats.getStatsComoMap().entrySet()) {
            statID = (Integer)entry.getKey();
            if (statID == 998 || statID == 997 || statID == 996 || statID == 994 || statID == 988 || statID == 987 || statID == 986 || statID == 985 || statID == 983 || statID == 960 || statID == 961 || statID == 962 || statID == 963 || statID == 964) continue;
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            String jet = "0d0+" + entry.getValue();
            stats = String.valueOf(stats) + Integer.toHexString(statID) + "#" + Integer.toHexString((Integer)entry.getValue()) + "#0#0#" + jet;
            primero = true;
        }
        for (Map.Entry entry : this._textoStats.entrySet()) {
            statID = (Integer)entry.getKey();
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            stats = statID == 800 || statID == 811 || statID == 961 || statID == 962 || statID == 960 || statID == 950 || statID == 951 ? String.valueOf(stats) + Integer.toHexString(statID) + "#0#0#" + (String)entry.getValue() : String.valueOf(stats) + Integer.toHexString(statID) + "#0#0#0#" + (String)entry.getValue();
            primero = true;
        }
        if (this._objevivoID != 0) {
            Set_Vivo set_Vivo = World.getObjevivos(this._objevivoID);
            if (set_Vivo == null) {
                this._objevivoID = 0;
            } else {
                if (primero) {
                    stats = String.valueOf(stats) + ",";
                }
                stats = String.valueOf(stats) + set_Vivo.convertirAString();
                primero = true;
            }
        }
        return stats;
    }

    public void addTextoStat(int stat, String texto) {
        this._textoStats.put(stat, texto);
    }

    public void addStat(int stat, int valor) {
        this._stats.addStat(stat, valor);
    }

    public String getNombreMision() {
        for (Map.Entry<Integer, String> entry : this._textoStats.entrySet()) {
            if (Integer.toHexString(entry.getKey()).compareTo("3dd") != 0) continue;
            return entry.getValue();
        }
        return null;
    }

    public int getRecompensaKamas() {
        for (Map.Entry<Integer, Integer> entry : this._stats.getStatsComoMap().entrySet()) {
            if (Integer.toHexString(entry.getKey()).compareTo("c2") != 0) continue;
            return entry.getValue();
        }
        return 0;
    }

    public Personagens.Stats getStats() {
        return this._stats;
    }

    public int getCantidad() {
        return this._cantidad;
    }

    public void setCantidad(int cantidad) {
        this._cantidad = cantidad;
    }

    public int getPosicion() {
        return this._posicion;
    }

    public void setPosicion(int posicion) {
        this._posicion = posicion;
    }

    public ObjetoModelo getModelo() {
        return this._modelo;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String stringObjetoConGui\u00f1o() {
        String str = String.valueOf(Integer.toHexString(this._id)) + "~" + Integer.toHexString(this._idObjModelo) + "~" + Integer.toHexString(this._cantidad) + "~" + (this._posicion == -1 ? "" : Integer.toHexString(this._posicion)) + "~" + this.convertirStatsAString();
        if (LesGuardians.ARMAS_ENCARNACAO.contains(this._idObjModelo)) {
            Incarnacao encarnacion = World.getEncarnacion(this._id);
            if (encarnacion == null) {
                str = String.valueOf(str) + ",76#1#0#0#0d0+1,77#1#0#0#0d0+1,7b#1#0#0#0d0+1,7e#1#0#0#0d0+1,29d#0#0#1";
            } else {
                String a = "#" + Integer.toHexString(encarnacion.getNivel() * 5) + "#0#0#0d0+" + encarnacion.getNivel() * 5;
                str = String.valueOf(str) + ",76" + a + ",77" + a + ",7b" + a + ",7e" + a + ",29d#0#0#" + Integer.toHexString(encarnacion.getNivel());
            }
        }
        return String.valueOf(str) + ";";
    }

    public String stringObjetoConPalo(int cantidad) {
        String str = String.valueOf(this._id) + "|" + cantidad + "|" + this._idObjModelo + "|" + this.convertirStatsAString();
        if (LesGuardians.ARMAS_ENCARNACAO.contains(this._idObjModelo)) {
            Incarnacao encarnacion = World.getEncarnacion(this._id);
            if (encarnacion == null) {
                str = String.valueOf(str) + ",76#1#0#0#0d0+1,77#1#0#0#0d0+1,7b#1#0#0#0d0+1,7e#1#0#0#0d0+1,29d#0#0#1";
            } else {
                String a = "#" + Integer.toHexString(encarnacion.getNivel() * 5) + "#0#0#0d0+" + encarnacion.getNivel() * 5;
                str = String.valueOf(str) + ",76" + a + ",77" + a + ",7b" + a + ",7e" + a + ",29d#0#0#" + Integer.toHexString(encarnacion.getNivel());
            }
        }
        return str;
    }

    public String convertirStatsAStringFM(String statsstr, Objeto obj, int agregar, boolean negativo) {
        int statID;
        String stats = "";
        boolean primero = false;
        for (SpellEffect spellEffect : obj._efectos) {
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            String[] infos = spellEffect.getArgs().split(";");
            try {
                stats = String.valueOf(stats) + Integer.toHexString(spellEffect.getEfectoID()) + "#" + infos[0] + "#" + infos[1] + "#0#" + infos[5];
            }
            catch (Exception e) {
                continue;
            }
            primero = true;
        }
        for (Map.Entry entry : obj._stats.getStatsComoMap().entrySet()) {
            statID = (Integer)entry.getKey();
            if (statID == 998 || statID == 997 || statID == 996 || statID == 994 || statID == 988 || statID == 987 || statID == 986 || statID == 985 || statID == 983 || statID == 960 || statID == 961 || statID == 962 || statID == 963 || statID == 964) continue;
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            if (Integer.toHexString(statID).compareTo(statsstr) == 0) {
                int newstats = 0;
                if (negativo) {
                    newstats = (Integer)entry.getValue() - agregar;
                    if (newstats < 1) {
                        continue;
                    }
                } else {
                    newstats = (Integer)entry.getValue() + agregar;
                }
                String jet = "0d0+" + newstats;
                stats = String.valueOf(stats) + Integer.toHexString(statID) + "#" + Integer.toHexString((Integer)entry.getValue() + agregar) + "#0#0#" + jet;
            } else {
                String jet = "0d0+" + entry.getValue();
                stats = String.valueOf(stats) + Integer.toHexString(statID) + "#" + Integer.toHexString((Integer)entry.getValue()) + "#0#0#" + jet;
            }
            primero = true;
        }
        for (Map.Entry entry : obj._textoStats.entrySet()) {
            statID = (Integer)entry.getKey();
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            stats = statID == 800 || statID == 811 || statID == 961 || statID == 962 || statID == 960 || statID == 950 || statID == 951 ? String.valueOf(stats) + Integer.toHexString(statID) + "#0#0#" + (String)entry.getValue() : String.valueOf(stats) + Integer.toHexString(statID) + "#0#0#0#" + (String)entry.getValue();
        }
        return stats;
    }

    public static int getStatBaseMax(ObjetoModelo objMod, String statsModif) {
        String[] split;
        String[] arrstring = split = objMod.getStringStatsObj().split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            String[] stats = s.split("#");
            if (stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) > 0 || stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) != 0) continue;
            int max = Integer.parseInt(stats[2], 16);
            if (max == 0) {
                max = Integer.parseInt(stats[1], 16);
            }
            return max;
        }
        return 0;
    }

    public static int getStatActual(Objeto objeto, String statsModif) {
        String[] split;
        String[] arrstring = split = objeto.convertirStatsAString().split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            String[] stats = s.split("#");
            if (stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) > 0 || stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) != 0) continue;
            int max = Integer.parseInt(stats[2], 16);
            if (max == 0) {
                max = Integer.parseInt(stats[1], 16);
            }
            return max;
        }
        return 0;
    }

    public String stringStatsFCForja(Objeto obj, double runa) {
        String stats = "";
        boolean primero = false;
        for (SpellEffect spellEffect : obj._efectos) {
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            String[] infos = spellEffect.getArgs().split(";");
            try {
                stats = String.valueOf(stats) + Integer.toHexString(spellEffect.getEfectoID()) + "#" + infos[0] + "#" + infos[1] + "#0#" + infos[5];
            }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            primero = true;
        }
        for (Map.Entry entry : obj._stats.getStatsComoMap().entrySet()) {
            int nuevosStats = 0;
            int statID = (Integer)entry.getKey();
            int valor = (Integer)entry.getValue();
            if (statID == 152 || statID == 154 || statID == 155 || statID == 157 || statID == 116 || statID == 153) {
                float chute;
                float a = (float)((double)valor * runa / 100.0);
                if (a < 1.0f) {
                    a = 1.0f;
                }
                if ((nuevosStats = (int)Math.floor(chute = (float)valor + a)) > Objeto.getStatBaseMax(obj._modelo, Integer.toHexString(statID))) {
                    nuevosStats = Objeto.getStatBaseMax(obj._modelo, Integer.toHexString(statID));
                }
            } else {
                if (statID == 127 || statID == 101) continue;
                float chute = (float)((double)valor - (double)valor * runa / 100.0);
                nuevosStats = (int)Math.floor(chute);
            }
            if (nuevosStats < 1) continue;
            String jet = "0d0+" + nuevosStats;
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            stats = String.valueOf(stats) + Integer.toHexString(statID) + "#" + Integer.toHexString(nuevosStats) + "#0#0#" + jet;
            primero = true;
        }
        for (Map.Entry entry : obj._textoStats.entrySet()) {
            if (primero) {
                stats = String.valueOf(stats) + ",";
            }
            stats = String.valueOf(stats) + Integer.toHexString((Integer)entry.getKey()) + "#0#0#0#" + (String)entry.getValue();
            primero = true;
        }
        return stats;
    }

    public ArrayList<SpellEffect> getEfectos() {
        return this._efectos;
    }

    public ArrayList<SpellEffect> getEfectosCriticos() {
        ArrayList<SpellEffect> efectos = new ArrayList<SpellEffect>();
        for (SpellEffect SE : this._efectos) {
            try {
                int max;
                boolean boost = false;
                if (SE.getEfectoID() == 101) {
                    boost = true;
                }
                String[] infos = SE.getArgs().split(";");
                if (boost) {
                    efectos.add(SE);
                    continue;
                }
                int min = Integer.parseInt(infos[0], 16);
                if (min > (max = Integer.parseInt(infos[1], 16))) {
                    max = min;
                }
                String jet = "0d0+" + (max + this._modelo.getBonusGC());
                String newArgs = String.valueOf(infos[0]) + ";" + infos[1] + ";0;-1;0;" + jet;
                efectos.add(new SpellEffect(SE.getEfectoID(), newArgs, 0, -1));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return efectos;
    }

    public static synchronized Objeto clonarObjeto(Objeto obj, int cantidad) {
        if (cantidad < 1) {
            cantidad = 1;
        }
        int tipo = obj.getModelo().getTipo();
        Objeto objeto = obj.getModelo().getTipo() == 18 ? new Objeto(0, obj.getIDModelo(), cantidad, -1, "320#0#0#a,326#0#0#0", 0) : (tipo == 33 || tipo == 42 || tipo == 49 || tipo == 69 || tipo == 12 ? new Objeto(0, obj._idObjModelo, cantidad, -1, obj._modelo._statsModelo, 0) : (tipo == 85 ? new Almas(0, cantidad, obj._idObjModelo, -1, obj._modelo._statsModelo) : (LesGuardians.ARMAS_ENCARNACAO.contains(obj._idObjModelo) ? new Objeto(0, obj._idObjModelo, cantidad, -1, obj._modelo._statsModelo, 0) : new Objeto(0, obj._idObjModelo, cantidad, -1, obj.convertirStatsAString(), obj.getEfectos(), 0))));
        return objeto;
    }

    public static String getRunas(Objeto Obj) {
        String runas = "";
        String statsModelo = Obj.convertirStatsAString();
        boolean esPrimero = true;
        String[] Splitted = statsModelo.split(",");
        int random = F�rmulas.getRandomValor(1, 6);
        int cantidad = 7 - random;
        String[] arrstring = Splitted;
        int n = Splitted.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            String[] Stats2 = s.split("#");
            if (Stats2[0].isEmpty()) continue;
            int statID = Integer.parseInt(Stats2[0], 16);
            int numero = 0;
            try {
                numero = Integer.parseInt(Stats2[4].replaceAll("0d0\\+", ""));
            }
            catch (Exception e) {
                continue;
            }
            if (numero <= 0 || random < Constantes.getValorStatRuna(statID) / 2) continue;
            boolean siguiente = true;
            int[] arrn = Constantes.ID_EFECTOS_ARMAS;
            int n2 = Constantes.ID_EFECTOS_ARMAS.length;
            for (int j = 0; j < n2; ++j) {
                int a = arrn[j];
                if (a != statID) continue;
                siguiente = false;
            }
            if (!siguiente) continue;
            if (!esPrimero) {
                runas = String.valueOf(runas) + ";";
            }
            runas = String.valueOf(runas) + Constantes.statDeRunas(statID, numero) + "," + cantidad;
            esPrimero = false;
        }
        return runas;
    }

    public void clearTodo() {
        this._stats = new Personagens.Stats();
        this._efectos.clear();
        this._textoStats.clear();
        this._hechizoStats.clear();
    }

    public void clearStats() {
        this._stats = new Personagens.Stats();
        this._textoStats.clear();
        this._hechizoStats.clear();
    }

    public static class ObjetoModelo {
        private int _idModelo;
        private String _statsModelo;
        private String _nombre;
        private int _tipo;
        private int _nivel;
        private int _peso;
        private int _precio;
        private int _setID;
        private int _precioVIP;
        private String _condiciones;
        private int _costePA;
        private int _alcanceMinimo;
        private int _alcanceMax;
        private int _porcentajeGC;
        private int _porcentajeFC;
        private int _bonusGC;
        private boolean _esDosManos;
        private ArrayList<Acao> _accionesDeUso = new ArrayList();
        private long _vendidos;
        private int _precioMedio;

        public ObjetoModelo(int id, String statModeloDB, String nombre, int tipo, int nivel, int peso, int precio, int setObjeto, String condiciones, String infoArma, int vendidos, int precioMedio, int precioVIP) {
            this._idModelo = id;
            this._statsModelo = statModeloDB;
            this._nombre = nombre;
            this._tipo = tipo;
            this._nivel = nivel;
            this._peso = peso;
            this._precio = precio;
            this._setID = setObjeto;
            this._condiciones = condiciones;
            this._costePA = -1;
            this._alcanceMinimo = 1;
            this._alcanceMax = 1;
            this._porcentajeGC = 100;
            this._porcentajeFC = 2;
            this._bonusGC = 0;
            this._vendidos = vendidos;
            this._precioMedio = precioMedio;
            this._precioVIP = precioVIP;
            try {
                String[] infos = infoArma.split(";");
                this._costePA = Integer.parseInt(infos[0]);
                this._alcanceMinimo = Integer.parseInt(infos[1]);
                this._alcanceMax = Integer.parseInt(infos[2]);
                this._porcentajeGC = Integer.parseInt(infos[3]);
                this._porcentajeFC = Integer.parseInt(infos[4]);
                this._bonusGC = Integer.parseInt(infos[5]);
                this._esDosManos = infos[6].equals("1");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        public void addAccion(Acao A) {
            this._accionesDeUso.add(A);
        }

        public boolean esDosManos() {
            return this._esDosManos;
        }

        public int getBonusGC() {
            return this._bonusGC;
        }

        public int getPrecioVIP() {
            return this._precioVIP;
        }

        public int getAlcMinimo() {
            return this._alcanceMinimo;
        }

        public int getAlcanceMax() {
            return this._alcanceMax;
        }

        public int getPorcGC() {
            return this._porcentajeGC;
        }

        public int getPorcFC() {
            return this._porcentajeFC;
        }

        public int getCostePA() {
            return this._costePA;
        }

        public int getID() {
            return this._idModelo;
        }

        public String getStringStatsObj() {
            return this._statsModelo;
        }

        public String getNombre() {
            return this._nombre;
        }

        public int getTipo() {
            return this._tipo;
        }

        public int getNivel() {
            return this._nivel;
        }

        public int getPeso() {
            return this._peso;
        }

        public int getPrecio() {
            return this._precio;
        }

        public int getSetID() {
            return this._setID;
        }

        public String getCondiciones() {
            return this._condiciones;
        }

        public Objeto crearObjDesdeModelo(int cantidad, boolean maxStats) {
            if (cantidad < 1) {
                cantidad = 1;
            }
            Objeto objeto = this._tipo == 18 ? new Objeto(0, this._idModelo, cantidad, -1, "320#0#0#a,326#0#0#0", 0) : (this._tipo == 33 || this._tipo == 42 || this._tipo == 49 || this._tipo == 69 || this._tipo == 12 ? new Objeto(0, this._idModelo, cantidad, -1, this._statsModelo, 0) : (this._tipo == 85 ? new Almas(0, cantidad, this._idModelo, -1, this._statsModelo) : (LesGuardians.ARMAS_ENCARNACAO.contains(this._idModelo) ? new Objeto(0, this._idModelo, cantidad, -1, this._statsModelo, 0) : new Objeto(0, this._idModelo, cantidad, -1, ObjetoModelo.generarStatsModeloDB(this._statsModelo, maxStats), this.generarEfectoModelo(this._statsModelo), 0))));
            return objeto;
        }

        public Objeto crearObjPosDesdeModelo(int cantidad, int pos, boolean maxStats) {
            Objeto objeto = new Objeto(0, this._idModelo, cantidad, pos, ObjetoModelo.generarStatsModeloDB(this._statsModelo, maxStats), this.generarEfectoModelo(this._statsModelo), 0);
            return objeto;
        }

        public static String generarStatsModeloDB(String statsModelo, boolean maxStats) {
            String statsObjeto = "";
            if (statsModelo.equals("") || statsModelo == null) {
                return statsObjeto;
            }
            String[] splitted = statsModelo.split(",");
            boolean primero = false;
            String[] arrstring = splitted;
            int n = splitted.length;
            for (int i = 0; i < n; ++i) {
                int valor;
                String[] stats;
                block11: {
                    String s = arrstring[i];
                    boolean esEfecto = false;
                    stats = s.split("#");
                    int statID = Integer.parseInt(stats[0], 16);
                    if (primero) {
                        statsObjeto = String.valueOf(statsObjeto) + ",";
                    }
                    int[] arrn = Constantes.ID_EFECTOS_ARMAS;
                    int n2 = Constantes.ID_EFECTOS_ARMAS.length;
                    for (int j = 0; j < n2; ++j) {
                        int a = arrn[j];
                        if (a != statID) continue;
                        statsObjeto = String.valueOf(statsObjeto) + stats[0] + "#" + stats[1] + "#" + stats[2] + "#0#" + stats[4];
                        esEfecto = true;
                        primero = true;
                        break;
                    }
                    if (esEfecto) continue;
                    String rango = "";
                    valor = 1;
                    if (statID == 811 || statID == 800) {
                        statsObjeto = String.valueOf(statsObjeto) + stats[0] + "#0#0#" + stats[3];
                        primero = true;
                        continue;
                    }
                    try {
                        rango = stats[4];
                        if (maxStats) {
                            try {
                                int min = Integer.parseInt(stats[1], 16);
                                int max = Integer.parseInt(stats[2], 16);
                                valor = min;
                                if (max != 0 && max > min) {
                                    valor = max;
                                }
                                break block11;
                            }
                            catch (Exception e) {
                                valor = F�rmulas.getRandomValor(rango);
                            }
                            break block11;
                        }
                        valor = F�rmulas.getRandomValor(rango);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                statsObjeto = String.valueOf(statsObjeto) + stats[0] + "#" + Integer.toHexString(valor) + "#0#" + stats[3] + "#0d0+" + valor;
                primero = true;
            }
            return statsObjeto;
        }

        private ArrayList<SpellEffect> generarEfectoModelo(String statsModelo) {
            String[] splitted;
            ArrayList<SpellEffect> efectos = new ArrayList<SpellEffect>();
            if (statsModelo.equals("") || statsModelo == null) {
                return efectos;
            }
            String[] arrstring = splitted = statsModelo.split(",");
            int n = splitted.length;
            for (int i = 0; i < n; ++i) {
                String s = arrstring[i];
                String[] stats = s.split("#");
                int statID = Integer.parseInt(stats[0], 16);
                int[] arrn = Constantes.ID_EFECTOS_ARMAS;
                int n2 = Constantes.ID_EFECTOS_ARMAS.length;
                for (int j = 0; j < n2; ++j) {
                    int a = arrn[j];
                    if (a != statID) continue;
                    String args = String.valueOf(stats[1]) + ";" + stats[2] + ";-1;-1;0;" + stats[4];
                    efectos.add(new SpellEffect(statID, args, 0, -1));
                }
            }
            return efectos;
        }

        public String stringDeStatsParaTienda() {
            String str = "";
            str = String.valueOf(str) + this._idModelo + ";";
            str = String.valueOf(str) + this._statsModelo;
            return str;
        }

        public void aplicarAccion(Personagens pj, Personagens objetivo, int objID, short celda) {
            for (Acao a : this._accionesDeUso) {
                a.aplicar(pj, objetivo, objID, celda);
            }
        }

        public int getPrecioPromedio() {
            return this._precioMedio;
        }

        public long getVendidos() {
            return this._vendidos;
        }

        public synchronized void nuevoPrecio(int cantidad, int precio) {
            long viejaVenta = this._vendidos;
            this._vendidos += (long)cantidad;
            this._precioMedio = (int)(((long)this._precioMedio * viejaVenta + (long)precio) / this._vendidos);
        }
    }
}


package at.ac.tuwien.hci.ghost.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Eine Wrapper-Klasse fŸr Datumsobjekte
 * 
 * Kann Datumswerte aus Strings (DefaultFormat dd.MM.yyyy), aus Objekten vom
 * Type java.util.Date, java.sql.Date, java.util.GregorianCalendar aus den
 * Millisekunden (long) sowie aus der Angabe von Tag (1-31), Monat(1-12) und
 * Jahr erzeugen.
 * 
 * @author Matthias
 */

public class Date implements Serializable {
	/** the serial version UID */
	private static final long serialVersionUID = 192026271093734748L;

	/** das gespeicherte Datum */
	private GregorianCalendar date = null;
	/** das Default-Format */
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private static SimpleDateFormat sdfFull = new SimpleDateFormat("EE, dd.MM.yyyy HH:mm");

	/**
	 * erzeugt ein Date-Objekt mit dem aktuellen Datum
	 */
	public Date() {
		date = new GregorianCalendar();
	}

	/**
	 * erzeugt ein Date-Objekt mit dem Datum im Format dd.MM.yyyy
	 * 
	 * @param date
	 *            Das Datum als String im Format dd.MM.yyyy
	 */
	public Date(String date) throws ParseException {
		set(date);
	}

	/**
	 * erzeugt ein Date-Objekt mit dem Datum im Format format
	 * 
	 * @param date
	 *            Das Datum als String
	 * @param format
	 *            Das Format des Datums
	 */
	public Date(String date, String format) throws ParseException {
		set(date, format);
	}

	/**
	 * erzeugt ein Datum aus einem java.util.Date-Objekt
	 * 
	 * @param date
	 *            Standard-Java-Datum
	 */
	public Date(java.util.Date date) {
		set(date);
	}

	/**
	 * erzeugt ein Datum aus einem GregorianCalendar-Objekt
	 * 
	 * @param date
	 *            GregorianCalendar-Datum
	 */
	public Date(GregorianCalendar date) {
		set(date);
	}

	/**
	 * erzeugt ein Datum aus den Ÿbergebenen Millisekunden
	 * 
	 * @param millis
	 *            die Millisekunden seit 1970 oder so
	 */
	public Date(long millis) {
		set(millis);
	}

	public Date(int day, int month, int year, int hour, int minute) {
		set(day, month, year,hour,minute);
	}

	/**
	 * erzeugt ein Datum aus der Kombination (Tag, Monat, Jahr)
	 * 
	 * @param day
	 *            Tag des Monats (1-31)
	 * @param month
	 *            Monat des Jahres (1 = JŠnner, 12 = Dezember)
	 * @param year
	 *            Das Jahr
	 */
	public Date(int day, int month, int year) {
		set(day, month, year,0,0);
	}

	/**
	 * setzt das Datum auf den heutigen Tag
	 */
	public void setToToday() {
		date = new GregorianCalendar();
	}

	/**
	 * Setzt das Datum neu
	 * 
	 * @param date
	 *            Datum im Format dd.MM.yyyy oder yyyy-MM-dd
	 */
	public void set(String date) throws ParseException {
		set(sdf.parse(date));
	}

	/**
	 * setzt das Datum neu
	 * 
	 * @param date
	 *            Datum als String
	 * @param format
	 *            Das Format des Datums
	 */
	public void set(String date, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);

		set(df.parse(date));
	}

	/**
	 * setzt das Datum neu
	 * 
	 * @param date
	 *            Das gewŸnschte Datum als Standard-Java-Datum
	 */
	@SuppressWarnings("deprecation")
	public void set(java.util.Date date) {
		set(date.getDate(), date.getMonth() + 1, date.getYear() + 1900, date.getHours(), date.getMinutes());
	}

	/**
	 * setzt das Datum neu
	 * 
	 * @param date
	 *            Das gewŸnschte Datum als GregorianCalendar
	 */
	public void set(GregorianCalendar date) {
		set(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR), date.get(Calendar.HOUR), date.get(Calendar.MINUTE));
	}

	/**
	 * setzt das Datum neu
	 * 
	 * @param millis
	 *            Millisekunden seit 1970
	 */
	public void set(long millis) {
		set(new java.util.Date(millis));
	}

	/**
	 * setzt das Datum neu
	 * 
	 * @param day
	 *            Tag des Monats (1-31)
	 * @param month
	 *            Monat des Jahres (1 = JŠnner, 12 = Dezember)
	 * @param year
	 *            Das Jahr
	 */
	public void set(int day, int month, int year, int hour, int minute) {
		// Monat wird in GregorianCalender mit 0 = JŠnner, 11 = Dezember
		// gespeichert ==> -1
		this.date = new GregorianCalendar(year, month - 1, day);
		setHour(hour);
		setMinute(minute);
	}

	/**
	 * Setzt die Stunde neu
	 * 
	 * @param hour
	 *            die neue Stunde
	 * @return
	 */
	public Date setHour(int hour) {
		this.date.set(Calendar.HOUR, hour);
		return this;
	}

	/**
	 * Setzt die Minute neu
	 * 
	 * @param minute
	 *            die neue Minute
	 * @return
	 */
	public Date setMinute(int minute) {
		this.date.set(Calendar.MINUTE, minute);
		return this;
	}

	/**
	 * Setzt den Tag neu
	 * 
	 * @param day
	 *            Der neue Tag
	 */
	public Date setDay(int day) {
		this.date.set(Calendar.DAY_OF_MONTH, day);
		return this;
	}

	/**
	 * Setzt den Monat neu
	 * 
	 * @param month
	 *            Der neue Monat
	 */
	public Date setMonth(int month) {
		this.date.set(Calendar.MONTH, month);
		return this;
	}

	/**
	 * Setzt das Jahr neu
	 * 
	 * @param year
	 *            Das neue Jahr
	 */
	public Date setYear(int year) {
		this.date.set(Calendar.YEAR, year);
		return this;
	}

	public int getHour() {
		return date.get(Calendar.HOUR);
	}

	public int getMinute() {
		return date.get(Calendar.MINUTE);
	}

	/**
	 * @return Der Tag des Monats (1-31)
	 */
	public int getDay() {
		return date.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return Der Monat des Jahres (1 = JŠnner, 12 = Dezember)
	 */
	public int getMonth() {
		// Monat wird in GregorianCalender mit 0 = JŠnner, 11 = Dezember
		// gespeichert ==> +1
		return date.get(Calendar.MONTH) + 1;
	}

	/**
	 * @return Das Jahr
	 */
	public int getYear() {
		return date.get(Calendar.YEAR);
	}

	/**
	 * Zugriffsmethode fŸr beliebige Datums-Metadaten
	 * 
	 * @param field
	 *            Das gewŸnschte Feld (zB Calendar.MONTH)
	 * @return Der Wert des gewŸnschte Felds
	 */
	public int get(int field) {
		// Monat wird in GregorianCalender mit 0 = JŠnner, 11 = Dezember
		// gespeichert ==> +1
		if (field == Calendar.MONTH)
			return date.get(field) + 1;
		return date.get(field);
	}

	/**
	 * @return Cast auf java.util.Date-Objekt
	 */
	public java.util.Date getAsJavaDefaultDate() {
		return date.getTime();
	}

	/**
	 * Berechnet die Jahresdifferenz zum übergebenen Datum
	 * 
	 * @param d
	 *            Ein Datum in der Zukunft
	 * @return Anzahl an Jahren die zwischen dem Datum und d liegen
	 */
	public long getYearDifference(Date d) {
		return d.getYear() - getYear();
	}

	/**
	 * Berechnet die Tagesdifferenz zum Ÿbergebenen Datum
	 * 
	 * @param d
	 *            Ein Datum beliebiges Datum
	 * @return Anzahl an Tagen die zwischen dem Datum und d liegen
	 */
	public long getDayDifference(Date d) {
		Calendar start = date.before(d.date) ? date : d.date;
		Calendar end = date.before(d.date) ? d.date : date;
		Calendar date = (Calendar) start.clone();
		long daysBetween = 0;

		while (date.before(end)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		
		return daysBetween - 1;
	}

	/**
	 * String-Darstellung im Default-Format
	 */
	@Override
	public String toString() {
		return sdf.format(date.getTime());
	}

	/**
	 * @return Wochentag, dd.mm.yyyy
	 */
	public String toFullString() {
		return sdfFull.format(date.getTime());
	}

	/**
	 * Adds amount to the specified field
	 * 
	 * @param field
	 *            Calendar.DAY/MONTH/YEAR
	 * @param amount
	 *            signed amount
	 */
	public void add(int field, int amount) {
		date.add(field, amount);
	}

	/**
	 * String-Darstellung im Ÿbergebenen Format
	 * 
	 * @param format
	 *            Das Format des Datums (zB yyyy-MM-dd)
	 * @return Das Datum im gewŸnschten Format als String
	 */
	public String toString(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date.getTime());
	}

}

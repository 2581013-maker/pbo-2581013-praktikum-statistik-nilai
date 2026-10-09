import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class StatistikNilai {

    // Sentinel: nilai penanda "sudah selesai" (seperti minggu lalu)
    static final int SELESAI = -1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Nilai disimpan di ArrayList<Integer> (tipe dalam <...> harus Integer, bukan int)
        ArrayList<Integer> daftar = new ArrayList<>();

        System.out.println("===== STATISTIK NILAI KELAS =====");
        System.out.println("Ketik -1 kalau sudah selesai.");

        // ---------- 1. PEMBACAAN (do-while + sentinel) ----------
        int input = 0;
        do {
            // Nomor prompt diambil dari size() + 1, bukan dari variabel pencacah terpisah.
            // Kalau nilai ditolak, size() tidak bertambah, sehingga nomor yang sama diminta lagi.
            System.out.print("Nilai ke-" + (daftar.size() + 1) + " : ");

            if (!scanner.hasNextInt()) {
                scanner.next(); // buang input yang bukan angka
                System.out.println("  Ditolak, harus berupa angka");
                continue;
            }
            input = scanner.nextInt();

            if (input == SELESAI) {
                break; // sentinel, jangan diproses sebagai nilai
            }

            if (input < 0 || input > 100) {
                System.out.println("  Ditolak, harus 0-100");
                continue; // lewati add(): nilai yang ditolak tidak masuk daftar
            }

            daftar.add(input);
        } while (input != SELESAI);

        System.out.println();

        // ---------- Kasus tidak ada nilai sah sama sekali ----------
        if (daftar.isEmpty()) {
            System.out.println("Tidak ada nilai yang dimasukkan, tidak ada yang bisa dihitung.");
            return;
        }

        // ---------- 2. JUMLAH & RATA-RATA ----------
        int total = 0;
        for (int x : daftar) {
            total += x;
        }
        double rataRata = (double) total / daftar.size();

        // ---------- 3. TERTINGGI & TERENDAH (loop sendiri, bukan Collections.max/min) ----------
        // Keduanya dimulai dari elemen PERTAMA (daftar.get(0)), bukan dari 0.
        // Untuk tertinggi, mulai dari 0 kebetulan masih benar. Tapi untuk terendah,
        // mulai dari 0 membuat hasilnya selalu 0, karena tidak ada nilai yang lebih kecil
        // dari 0. Dengan mulai dari elemen pertama, nilai awal pasti salah satu nilai asli.
        int tertinggi = daftar.get(0);
        int terendah = daftar.get(0);
        for (int x : daftar) {
            if (x > tertinggi) tertinggi = x;
            if (x < terendah) terendah = x;
        }

        // ---------- 4. DI ATAS RATA-RATA (putaran kedua) ----------
        // Tidak bisa dihitung di putaran pertama (saat membaca), karena saat nilai
        // pertama dibaca, rata-rata belum diketahui: rata-rata baru ada setelah SEMUA
        // nilai terbaca. Makanya nilai harus disimpan dulu, lalu dibandingkan di putaran kedua.
        int diAtasRataRata = 0;
        for (int x : daftar) {
            if (x > rataRata) diAtasRataRata++;
        }

        // ---------- 5. DISTRIBUSI GRADE (int[5], index = nomor grade) ----------
        // index 0 = A, 1 = B, 2 = C, 3 = D, 4 = E
        String[] namaGrade = {"A", "B", "C", "D", "E"};
        int[] jumlahGrade = new int[5];
        for (int x : daftar) {
            if (x >= 90)      jumlahGrade[0]++;
            else if (x >= 80) jumlahGrade[1]++;
            else if (x >= 70) jumlahGrade[2]++;
            else if (x >= 60) jumlahGrade[3]++;
            else              jumlahGrade[4]++;
        }

        // ---------- 6. DAFTAR TERURUT (dari SALINAN) ----------
        ArrayList<Integer> terurut = new ArrayList<>(daftar);
        Collections.sort(terurut);

        // ---------- CETAK HASIL ----------
        // Locale Indonesia dipakai eksplisit supaya desimal selalu berkoma (75,50),
        // apa pun locale laptopnya.
        Locale indonesia = Locale.forLanguageTag("id-ID");

        System.out.println("Nilai tersimpan : " + daftar);
        System.out.println("Jumlah          : " + daftar.size());
        System.out.println(String.format(indonesia, "Rata-rata       : %.2f", rataRata));
        System.out.println("Tertinggi       : " + tertinggi);
        System.out.println("Terendah        : " + terendah);
        System.out.println("Di atas rata2   : " + diAtasRataRata + " orang");

        System.out.print("Distribusi      : ");
        for (int i = 0; i < jumlahGrade.length; i++) {
            System.out.print(namaGrade[i] + "=" + jumlahGrade[i]);
            if (i < jumlahGrade.length - 1) System.out.print(" ");
        }
        System.out.println();

        System.out.println("Terurut         : " + terurut);
        // Urutan asli dicetak untuk membuktikan daftar asli tidak ikut terurut
        System.out.println("Urutan asli     : " + daftar);

        scanner.close();
    }
}
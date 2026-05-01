package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) {
            return;
        }

        int n = sc.nextInt();

        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < n; i++) {
                int id = sc.nextInt();
                double suma = Double.parseDouble(sc.next());
                String data = sc.next();
                TipTranzactie tip = TipTranzactie.valueOf(sc.next());

                writeRecord(out, id, suma, data, tip, Status.PENDING);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (sc.hasNext()) {
                String comanda = sc.next();

                if (comanda.equals("READ")) {
                    int idx = sc.nextInt();
                    printRecord(raf, idx);
                } else if (comanda.equals("UPDATE")) {
                    int idx = sc.nextInt();
                    Status status = Status.valueOf(sc.next());

                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusToByte(status));

                    System.out.println("Updated [" + idx + "]: " + status);
                } else if (comanda.equals("PRINT_ALL")) {
                    for (int i = 0; i < n; i++) {
                        printRecord(raf, i);
                    }
                } else if (comanda.equals("QUIT")) {
                    break;
                }
            }
        }
    }

    private static void writeRecord(DataOutputStream out, int id, double suma,
                                    String data, TipTranzactie tip, Status status) throws IOException {
        byte[] record = new byte[RECORD_SIZE];

        ByteBuffer buffer = ByteBuffer.wrap(record);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(id);
        buffer.putDouble(suma);

        byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < 10; i++) {
            if (i < dataBytes.length) {
                record[12 + i] = dataBytes[i];
            } else {
                record[12 + i] = ' ';
            }
        }

        record[22] = tipToByte(tip);
        record[23] = statusToByte(status);

        out.write(record);
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int id = buffer.getInt();
        double suma = buffer.getDouble();

        String data = new String(record, 12, 10, StandardCharsets.US_ASCII).trim();
        TipTranzactie tip = byteToTip(record[22]);
        Status status = byteToStatus(record[23]);

        System.out.printf(Locale.US,
                "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip, suma, status);
    }

    private static byte tipToByte(TipTranzactie tip) {
        if (tip == TipTranzactie.CREDIT) {
            return 0;
        }
        return 1;
    }

    private static TipTranzactie byteToTip(byte value) {
        if (value == 0) {
            return TipTranzactie.CREDIT;
        }
        return TipTranzactie.DEBIT;
    }

    private static byte statusToByte(Status status) {
        if (status == Status.PENDING) {
            return 0;
        }
        if (status == Status.PROCESSED) {
            return 1;
        }
        return 2;
    }

    private static Status byteToStatus(byte value) {
        if (value == 0) {
            return Status.PENDING;
        }
        if (value == 1) {
            return Status.PROCESSED;
        }
        return Status.REJECTED;
    }
}

enum Status {
    PENDING,
    PROCESSED,
    REJECTED
}
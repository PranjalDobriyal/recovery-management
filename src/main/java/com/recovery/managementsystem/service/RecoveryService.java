package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Recovery;
import com.recovery.managementsystem.repository.ClientRepository;
import com.recovery.managementsystem.repository.RecoveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecoveryService {

    @Autowired
    private RecoveryRepository recoveryRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<Recovery> findAll() {
        return recoveryRepository.findAll();
    }

    public void importCsv(MultipartFile file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true; // To skip header

            List<Recovery> recoveries = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header row
                }

                String[] data = line.split(",");

                Recovery recovery = new Recovery();
                recovery.setClient(clientRepository.findById(Integer.parseInt(data[0])).orElse(null));
                recovery.setRecoveryStage(data[1].toUpperCase());
                recovery.setAmountToRecover(new BigDecimal(data[2]));
                recovery.setRecoveredAmount(new BigDecimal(data[3]));
                recovery.setRecoveryType(data[4]);
                recovery.setNotes(data[5]);
                recovery.setStartDate(parseDate(data[6])); // Use utility method for date parsing
                recovery.setDueDate(parseDate(data[7]));
                recovery.setLastFollowUpDate(parseDate(data[8]));
                recovery.setNextFollowUpDate(parseDate(data[9]));
                recovery.setFollowUpCount(Integer.parseInt(data[10]));
                recovery.setActionTaken(data[11]);
                recovery.setCommunicationMode(data[12].toUpperCase());
                recovery.setAgreementSigned(Boolean.parseBoolean(data[13]));
                recovery.setLegalActionRequired(Boolean.parseBoolean(data[14]));
                recovery.setEscalationLevel(data[15].toUpperCase());
                recovery.setRecoveryPriority(data[16].toUpperCase());
                recovery.setAssignedBy(Integer.parseInt(data[17]));
                recovery.setCreatedAt(parseDate(data[18]));
                recovery.setUpdatedAt(parseDate(data[19]));

                recoveries.add(recovery);
            }
            recoveryRepository.saveAll(recoveries);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing CSV file: " + e.getMessage());
        }
    }

    /**
     * Utility method to parse dates in multiple formats.
     *
     * @param dateString The date string to parse.
     * @return Parsed LocalDate object.
     * @throws DateTimeParseException If the date cannot be parsed with any format.
     */
    private LocalDate parseDate(String dateString) {
        List<DateTimeFormatter> formatters = new ArrayList<>();
        formatters.add(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formatters.add(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Continue to the next formatter
            }
        }
        throw new DateTimeParseException("Unable to parse date: " + dateString, dateString, 0);
    }

	public Recovery findById(Integer id) {
		return recoveryRepository.findByRecoveryId(id);
	}

	public void save(Recovery recovery) {
		recoveryRepository.save(recovery);
		
	}
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.samaddico.fileuploader.repository;

import com.samaddico.fileuploader.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author addico
 */

@Transactional
public interface FileRepository extends JpaRepository<File, Long> {
    
}

package com.bobby.poll.controllers;

import com.bobby.poll.domain.Poll;
import com.bobby.poll.exception.ResourceNotFoundException;
import com.bobby.poll.repository.PollRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class PollController {

    private final PollRepository pollRepository;

    @Inject
    public PollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    protected void verifyPoll(Long pollId) throws ResourceNotFoundException {
        pollRepository.findById(pollId);
        if(pollId.equals(pollId)) {
            throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
        }
    }

    @RequestMapping(value="/polls", method= RequestMethod.GET)
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        Iterable<Poll> allPolls = pollRepository.findAll();
        return new ResponseEntity<>(pollRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value="/polls", method=RequestMethod.POST)
    public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll) {
        poll = pollRepository.save(poll);
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}").buildAndExpand(poll.getId()).toUri();
        responseHeaders.setLocation(newPollUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.GET)
    public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        pollRepository.findById(pollId);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.PUT)
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
        verifyPoll(pollId);
        pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/polls/{pollId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        pollRepository.deleteById(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

Assignment 4
------------

# Team Members
Lena Högger
Blerta Cerkezi

# GitHub link to your (forked) repository (if submitting through GitHub)

...

# Task 2

1. Why did message D have to be buffered and can we now always guarantee that all clients
   display the same message order?

Message D has vector clock [2, 1, 1, 0], indicating client 1 had seen messages from clients 2 and 3 before sending D. If D arrives before message C (from client 3), the receiver's clock for client 3 is 0, while D shows client 1 had seen client 3's message (value 1). This violates causal ordering (senderClock[3] > receiverClock[3]), so D must be buffered until C arrives.

Vector clocks guarantee causal ordering for causally related messages, but concurrent messages may still be delivered in different orders across clients. For total ordering, we would need an additional mechanism like a sequencer.

2. Note that the chat application uses UDP. What could be an issue with this design choice—and
   how would you fix it?

UDP issues: packet loss (no delivery guarantee), duplicate packets, and no flow control. This can cause missing or duplicate messages.

Solutions: (1) Switch to TCP for reliable delivery, (2) implement a reliable UDP layer with ACKs and retransmissions, or (3) use a message broker that handles reliability and ordering.
   
# Task 3

1. What is potential causality in Distributed Systems, and how can you model it? Why
   “potential causality” and not just “causality”?

Potential causality describes which events might be causally related in a distributed system.
We model it with Lamport’s happened-before relation:
   - events in the same process: A → B 
   - send → receive of a message: A → B
   - transitive closure
   - 
We say “potential” because processes cannot know whether an event truly caused another. They only know that causality 
could exist based on message flow, not that it definitely did.



2. If you look at your implementation of Task 2.1, can you think of one limitation of Vector Clocks? How would you overcome the limitation?

A key limitation is that the size of vector clocks increases with the number of processes.
This results in large, expensive timestamps in systems with many nodes.
-> One solution is to use dynamic vector clocks, interval tree clocks or other compact logical clock schemes that scale 
better and adapt to changes in membership.




3. Figure 4 shows an example of enforcing causal communication using Vector Clocks. You can find a detailed explanation of this example and the broadcast algorithm being used in
   the Distributed Systems book by van Steen and Tannenbaum (see Chapter 5.2.2, page 270). Would you achieve the same result if you used the same broadcast algorithm but replaced
   Vector Clocks with Lamport Clocks? If not, why not? Explain briefly. 

No, Lamport clocks only provide a total order, not a true causal order. They cannot distinguish between concurrent and 
causally related events. Consequently, a Lamport-based broadcast may deliver messages in a way that violates causality, 
whereas vector clocks correctly enforce causal delivery.


# Declaration of Tools 
We used ChatGPT to help us understand the concepts and debug parts of the code.
We also used DeepL Write to improve the clarity and correctness of English answers.
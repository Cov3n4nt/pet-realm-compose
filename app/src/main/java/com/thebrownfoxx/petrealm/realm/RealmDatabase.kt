package com.thebrownfoxx.petrealm.realm

import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Pet
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.mongodb.kbson.ObjectId
import com.thebrownfoxx.petrealm.realm.models.RealmOwner
import com.thebrownfoxx.petrealm.realm.models.RealmPet
import com.thebrownfoxx.petrealm.realm.models.RealmPetType
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.nio.file.Files.delete

class RealmDatabase {
    private val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf(RealmPet::class, RealmOwner::class, RealmPetType::class)
        ).schemaVersion(1)
            .initialData {
                copyToRealm(RealmPetType().apply { name = "Cat" })
                copyToRealm(RealmPetType().apply { name = "Dog" })
            }
            .build()
        Realm.open(config)
    }

    fun getAllPetTypes(): Flow<List<RealmPetType>> =
        realm.query<RealmPetType>().asFlow().map { it.list }


    fun getAllPets(): Flow<List<RealmPet>> = realm.query<RealmPet>().asFlow().map { it.list }

    suspend fun editPet(pet: Pet, petName: String, petAge: Int, petType: String, ownerName: String){
        withContext(Dispatchers.IO){
            realm.write {
                val petResult: RealmPet? = realm.query<RealmPet>("id == $0", ObjectId(pet.id)).first().find()
                if(petResult != null){
                    val petRealm = findLatest(petResult)
                    petRealm?.apply {
                        this.name = petName
                        this.age = petAge
                        this.type = petType
                    }
                }
                else{
                    throw IllegalStateException("Pet not found!")
                }
            }
        }
    }

    suspend fun updateOwner(owner: Owner, newName: String) {
        realm.write {
            val ownerResult: RealmOwner? = realm.query<RealmOwner>("id == $0", ObjectId(owner.id)).first().find()
            if (ownerResult != null) {
                val ownerRealm = findLatest(ownerResult)
                ownerRealm?.apply {
                    this.name = newName
                }
            }
            else{
                throw IllegalStateException("Owner not found!")
            }
        }
    }

    suspend fun addOwner(ownerName: String, pet: Pet) {
        withContext(Dispatchers.IO) {
            realm.write {
               val ownerResult: RealmOwner? = realm.query<RealmOwner>("name == $0", ownerName).first().find()

                if(ownerResult != null){
                    val existingOwner = findLatest(ownerResult)
                    val petResult: RealmPet? = realm.query<RealmPet>("id == $0", ObjectId(pet.id)).first().find()
                    val existingPet = findLatest(petResult!!)

                    existingOwner?.pets?.add(existingPet!!)
                    existingPet!!.owner = existingOwner

                }
                else{
                    val petResult: RealmPet? = realm.query<RealmPet>("id == $0", ObjectId(pet.id)).first().find()
                    val existingPet = findLatest(petResult!!)

                    val owner = RealmOwner().apply {
                        this.name = ownerName
                        this.pets.add(existingPet!!)
                    }
                    val managedOwner = copyToRealm(owner)
                    existingPet?.owner = findLatest(managedOwner)
                }

            }
        }
    }


    suspend fun addPet(
        name: String,
        age: Int,
        type: String = "",
        ownerName: String = "",
    ) {
        withContext(Dispatchers.IO) {
            realm.write {
                val pet = RealmPet().apply {
                    this.name = name
                    this.age = age
                    this.type = type
                }
                val managedPet = copyToRealm(pet)
                if (ownerName.isNotEmpty()) {
                    val ownerResult: RealmOwner? =
                        realm.query<RealmOwner>("name == $0", ownerName).first().find()

                    if (ownerResult == null) {
                        val owner = RealmOwner().apply {
                            this.name = ownerName
                            this.pets.add(managedPet)
                        }
                        val managedOwner = copyToRealm(owner)
                        managedPet.owner = managedOwner
                    }

                    else {
                        findLatest(ownerResult)?.pets?.add(managedPet)
                        findLatest(managedPet)?.owner = findLatest(ownerResult)
                    }
                }
            }
        }
    }

    suspend fun deletePet(id: ObjectId) {
        withContext(Dispatchers.IO) {
            realm.write {
                query<RealmPet>("id == $0", id)
                    .first()
                    .find()
                    ?.let { delete(it) }
                    ?: throw IllegalStateException("Pet not found!")
            }
        }
    }

    fun getAllOwners(): Flow<List<RealmOwner>> = realm.query<RealmOwner>().asFlow().map { it.list }

    suspend fun deletOwner(id: ObjectId){
        withContext(Dispatchers.IO){
            realm.write {
                query<RealmOwner>("id == $0",id)
                    .first()
                    .find()
                    ?.let { delete(it) }
                    ?: throw IllegalStateException("Owner not found!")
            }
        }
    }

    suspend fun disownPet(pet:Pet){
        withContext(Dispatchers.IO){
            realm.write {
                val petResult = query<RealmPet>("id ==$0",ObjectId(pet.id)).first().find()

                if(petResult!=null){
                    val petRealm = findLatest(petResult)
                    petRealm?.owner?.pets?.remove(petRealm)
                    petRealm?.owner = null
                }
                else{
                    throw IllegalStateException("Pet not found!")
                }
            }
        }
    }

}
